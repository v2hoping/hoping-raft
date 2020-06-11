package com.v2hoping.core.rpc;

import com.alipay.sofa.rpc.api.future.SofaResponseFuture;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.v2hoping.core.consensus.AppendLogRequest;
import com.v2hoping.core.consensus.AppendLogResponse;
import com.v2hoping.core.consensus.VoteRequest;
import com.v2hoping.core.consensus.VoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by houping wang on 2020/5/22
 * 远程调用
 *
 * @author houping wang
 */
public class DefaultPeer implements Peer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPeer.class);

    /**
     * IP地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 集群
     */
    private String cluster;

    /**
     * 远程客户单
     */
    private PeerRpc peerRpc;

    @Override
    public VoteResponse vote(VoteRequest voteRequest) {
        return peerRpc.vote(voteRequest);
    }

    @Override
    public AppendLogResponse appendLog(AppendLogRequest appendLogRequest) {
        return peerRpc.appendLog(appendLogRequest);
    }

    public DefaultPeer(String host, String cluster) {
        String[] units = host.split(":");
        this.ip = units[0];
        this.port = units[1];
        this.cluster = cluster;
    }

    public DefaultPeer(String ip, String port, String cluster) {
        this.ip = ip;
        this.port = port;
        this.cluster = cluster;
    }

    @Override
    public void initRpc() {
        peerRpc = new ConsumerConfig<PeerRpc>()
                // 指定接口
                .setInterfaceId(PeerRpc.class.getName())
                // 指定协议
                .setProtocol("bolt")
                .setInvokeType("future")
                .setRepeatedReferLimit(-1)
                // 指定直连地址
                .setDirectUrl("bolt://" + this.getHost())
                .refer();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("[hoping-raft]Create Peer Rpc Client Success! host:" + this.getHost());
        }
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getCluster() {
        return cluster;
    }

    @Override
    public String getHost() {
        return this.ip + ":" + this.port;
    }

    @Override
    public String getId() {
        return cluster + "#" + this.getHost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultPeer that = (DefaultPeer) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(cluster, that.cluster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, cluster);
    }
}
