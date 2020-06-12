package com.v2hoping.rpc;

import com.alipay.remoting.InvokeCallbackListener;
import com.alipay.sofa.rpc.api.future.SofaResponseFuture;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.alipay.sofa.rpc.message.bolt.BoltFutureInvokeCallback;
import com.alipay.sofa.rpc.message.bolt.BoltResponseFuture;
import com.v2hoping.rpc.RpcService;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by houping wang on 2020/5/8
 *
 * @author houping wang
 */
public class RpcClientTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConsumerConfig<RpcService> consumerConfig = new ConsumerConfig<RpcService>()
                .setInterfaceId(RpcService.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址

        // 生成代理类
        RpcService helloService = consumerConfig.refer();
        System.out.println("客户端:" + helloService.say("world"));
    }

    @Test
    public void future() {
        ConsumerConfig<RpcService> consumerConfig = new ConsumerConfig<RpcService>()
                .setInterfaceId(RpcService.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setInvokeType("future")
                .setTimeout(20000)
                .setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址
        // 生成代理类
        RpcService helloService = consumerConfig.refer();
        while (true) {
            System.out.println("客户端:" + helloService.say("world"));
            try {
                Object response = SofaResponseFuture.getResponse(5000, false);
                System.out.println("客户端:" + response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000000);
            } catch (Exception e) {
            }
        }
    }
}
