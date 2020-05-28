package com.v2hoping.rpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.v2hoping.rpc.RpcImp;
import com.v2hoping.rpc.RpcService;

/**
 * Created by houping wang on 2020/5/8
 *
 * @author houping wang
 */
public class RpcServerTest {

    public static void main(String[] args) {
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(12200) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程

        ProviderConfig<RpcService> providerConfig = new ProviderConfig<RpcService>()
                .setInterfaceId(RpcService.class.getName()) // 指定接口
                .setRef(new RpcImp()) // 指定实现
                .setServer(serverConfig); // 指定服务端

        providerConfig.export(); // 发布服务
    }
}
