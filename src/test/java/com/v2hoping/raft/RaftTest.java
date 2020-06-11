package com.v2hoping.raft;

import com.alipay.sofa.rpc.api.future.SofaResponseFuture;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.v2hoping.core.consensus.AppendLogRequest;
import com.v2hoping.core.consensus.VoteRequest;
import com.v2hoping.core.rpc.PeerRpc;
import com.v2hoping.core.server.DefaultServer;
import com.v2hoping.core.server.Server;
import com.v2hoping.rpc.RpcService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by houping wang on 2020/6/4
 *
 * @author houping wang
 */
public class RaftTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaftTest.class);

    @Test
    public void test() {
        ConsumerConfig<PeerRpc> consumerConfig = new ConsumerConfig<PeerRpc>()
                .setInterfaceId(PeerRpc.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setInvokeType("future")
                .setDirectUrl("bolt://127.0.0.1:8000"); // 指定直连地址
        // 生成代理类
        PeerRpc peerRpc = consumerConfig.refer();
        while (true) {
            System.out.println("客户端:" + peerRpc.vote(new VoteRequest()));
            try {
                Object response = SofaResponseFuture.getResponse(10000, false);
                System.out.println("客户端:" + response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
    }
}
