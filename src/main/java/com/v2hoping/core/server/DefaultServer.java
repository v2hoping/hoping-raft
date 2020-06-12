package com.v2hoping.core.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sofa.rpc.api.future.SofaResponseFuture;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.v2hoping.common.ConfigureLoader;
import com.v2hoping.common.RaftExecutor;
import com.v2hoping.common.RandomUtils;
import com.v2hoping.core.consensus.AppendLogRequest;
import com.v2hoping.core.consensus.AppendLogResponse;
import com.v2hoping.core.consensus.VoteRequest;
import com.v2hoping.core.consensus.VoteResponse;
import com.v2hoping.core.log.DefaultLog;
import com.v2hoping.core.log.Log;
import com.v2hoping.core.log.LogEntry;
import com.v2hoping.core.machine.StateMachine;
import com.v2hoping.core.rpc.DefaultPeer;
import com.v2hoping.core.rpc.Peer;
import com.v2hoping.core.rpc.PeerRpc;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.nimbus.State;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by houping wang on 2020/5/13
 *
 * @author houping wang
 */
public class DefaultServer implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServer.class);

    /**
     * 服务器最后一次知道的任期号（初始化为最大索引任期值，持续递增）
     */
    private volatile long currentTerm;

    /**
     * 在当前获得选票的候选人的 Id(通过心跳维持)
     */
    private volatile String votedFor;

    /**
     * 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号
     */
    private Log log;

    /**
     * 非持久化数据
     */
    private Long commitIndex;

    /**
     * 最后被应到状态机的
     */
    private Long lastApplied;

    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    private Map<PeerRpc, Long> nextIndex;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    private Map<PeerRpc, Long> matchIndex;

    /*** 以下定义服务需要值 ***/
    /**
     * 是否已开启
     */
    private volatile boolean stated = false;

    /**
     * 集群机器
     */
    private Set<Peer> peers = null;

    /**
     * 集群ID和对象映射
     */
    private Map<String, Peer> peerMap = null;

    /**
     * 自身
     */
    private Peer self = null;

    /**
     * 当前服务状态
     */
    private ServerState state;

    /**
     * 主
     */
    private Peer leader;

    private Lock electionLock = new ReentrantLock();

    @Override
    public void start() {
        //如果服务已经开启则直接返回，防止重复开启服务
        if (stated) {
            return;
        }
        //初始化配置
        String clusterName = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_NAME);
        String clusterServers = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_SERVERS);
        String clusterSelf = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_SELF);
        String[] hosts = clusterServers.split(",");
        //设置状态
        this.state = ServerState.Follower;
        //设置任期
        this.currentTerm = 0L;
        this.lastApplied = 0L;
        //自身机器
        self = new DefaultPeer(clusterSelf, clusterName);
        //其他节点加入，不包含本身
        peers = new HashSet<>();
        for (String host : hosts) {
            DefaultPeer newPeer = new DefaultPeer(host, clusterName);
            if (!self.equals(newPeer)) {
                newPeer.initRpc();
                peers.add(newPeer);
            }
        }
        //集群机器映射
        peerMap = new HashMap<>();
        peerMap.put(self.getId(), self);
        for (Peer peer : peers) {
            peerMap.put(peer.getId(), peer);
        }
        //获得日志处理器
        log = DefaultLog.getInstance();
        //日志索引
        nextIndex = new HashMap<>(16);
        matchIndex = new HashMap<>(16);
        //启动服务
        this.initServerRpc();
        //启动定时器
        this.voteTimer();
    }

    private void initServerRpc() {
        ServerConfig serverConfig = new ServerConfig()
                // 设置一个协议，默认bolt
                .setProtocol("bolt")
                // 本机端口
                .setPort(Integer.parseInt(self.getPort()))
                // 非守护线程
                .setDaemon(false);
        ProviderConfig<PeerRpc> providerConfig = new ProviderConfig<PeerRpc>()
                // 指定接口
                .setInterfaceId(PeerRpc.class.getName())
                // 指定实现
                .setRef(this)
                // 指定服务端
                .setServer(serverConfig);
        // 发布服务
        providerConfig.export();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[hoping-raft]local server rpc runtime. host:" + self.getHost());
        }
    }

    @Override
    public VoteResponse vote(VoteRequest voteRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(voteRequest.getCandidateId() + " start vote!");
        }
        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setTerm(currentTerm);
        voteResponse.setVoteGranted(false);
        try {
            if (!electionLock.tryLock(450, TimeUnit.MILLISECONDS)) {
                return voteResponse;
            }

            //小于当前任期，则拒绝
            if (voteRequest.getTerm() < currentTerm) {
                return voteResponse;
            }
            //已投票，并且非已投票人选发来请求则拒绝
            if (this.votedFor != null && !this.votedFor.equals(voteRequest.getCandidateId())) {
                return voteResponse;
            }
            //获取最后一次日志
            LogEntry lastLog = this.log.getLastLog();
            if (lastLog != null) {
                //最新的将被应用
                if (lastLog.getTerm() > voteRequest.getTerm()) {
                    return voteResponse;
                }
                if (lastLog.getIndex() > voteRequest.getLastLogIndex()) {
                    return voteResponse;
                }
            }
            //投票成功
            this.votedFor = voteRequest.getCandidateId();
            this.leader = this.peerMap.get(this.votedFor);
            this.state = ServerState.Follower;
            this.currentTerm = voteRequest.getTerm();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(votedFor + " get vote!");
            }
        } catch (InterruptedException e) {
            return voteResponse;
        } finally {
            electionLock.unlock();
        }
        return voteResponse;
    }

    @Override
    public AppendLogResponse appendLog(AppendLogRequest appendLogRequest) {
        return null;
    }

    private void voteTimer() {
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "投票线程" + System.currentTimeMillis());
            }
        }, 1, TimeUnit.MILLISECONDS, 1024);
        Integer delay = RandomUtils.nextInt(150, 500);
        hashedWheelTimer.newTimeout(new ElectionTimerTask(delay), delay, TimeUnit.MILLISECONDS);
    }

    class ElectionTimerTask implements TimerTask {

        private long delay;

        public ElectionTimerTask(long delay) {
            this.delay = delay;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (state == ServerState.Leader) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("currentTerm:{},self is leader, no run election", currentTerm);
                }
            }
            final long beginTime = System.currentTimeMillis();
            //自己投票给自己，转变为候选者
            currentTerm = currentTerm + 1;
            state = ServerState.Candidate;
            votedFor = self.getId();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("currentTerm:" + currentTerm + ",self change " + ServerState.Candidate);
            }
            //发送请求给其他服务
            final VoteRequest voteRequest = new VoteRequest();
            voteRequest.setCandidateId(self.getId());
            LogEntry lastLog = log.getLastLog();
            voteRequest.setLastLogTerm(lastLog != null ? lastLog.getIndex() : -1);
            voteRequest.setLastLogIndex(lastLog != null ? lastLog.getIndex() : -1);
            voteRequest.setTerm(currentTerm);
            final List<Future> futures = new ArrayList<>();
            for (final Peer peer : peers) {
                Future future = RaftExecutor.submitElection(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            return peer.vote(voteRequest);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                            return null;
                        }
                    }
                });
                futures.add(future);
            }
            final long middleTime = System.currentTimeMillis();
            //异步获得结果
            final AtomicInteger gainVote = new AtomicInteger();
            final CountDownLatch latch = new CountDownLatch(futures.size());
            for (final Future future : futures) {
                RaftExecutor.submitElection(new Runnable() {
                    @Override
                    public void run() {
                        VoteResponse o = null;
                        try {
                            long limit = delay - (middleTime - beginTime);
                            LOGGER.debug("get value limit time: {}", limit);
                            o = (VoteResponse) future.get(limit, TimeUnit.MILLISECONDS);
                            //如果投票成功增加选票
                            if (o == null) {
                                return;
                            }
                            if (o.getVoteGranted()) {
                                gainVote.getAndIncrement();
                            } else {
                                //更新自己的Term
                                if (o.getTerm() > currentTerm) {
                                    currentTerm = o.getTerm();
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        } finally {
                            LOGGER.info("vote result:{}", JSON.toJSONString(o));
                            latch.countDown();
                        }
                    }
                });
            }
            //等待
            latch.await();
            //计算投票
            int halfVote = (peers.size() + 1) / 2;
            if (gainVote.intValue() + 1 > halfVote) {
                LOGGER.info("node {} become leader ", self.getId());
                state = ServerState.Leader;
                leader = peerMap.get(self.getId());
                votedFor = "";
            } else {
                votedFor = null;
            }
            //下一轮投票
            Integer delay = RandomUtils.nextInt(150, 500);
            timeout.timer().newTimeout(this, delay, TimeUnit.MILLISECONDS);
        }
    }
}
