package com.v2hoping.core.rpc;

/**
 * Created by houping wang on 2020/6/3
 *
 * @author houping wang
 */
public interface Peer extends PeerRpc{

    /**
     * 获得服务IP
     * @return IP
     */
    String getIp();

    /**
     * 获得服务端口
     * @return 端口
     */
    String getPort();

    /**
     * 获得集群名称
     * @return 集群名称
     */
    String getCluster();

    /**
     * 获得host地址
     * @return host
     */
    String getHost();

    /**
     * 获得ID
     * @return 获得ID
     */
    String getId();

    /**
     * 初始化RPC服务
     */
    void initRpc();
}
