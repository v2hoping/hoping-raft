package com.v2hoping.core.server;

import com.v2hoping.core.log.Log;
import com.v2hoping.core.rpc.PeerRpc;

/**
 * Created by houping wang on 2020/5/9
 *
 * @author houping wang
 */
public interface Server extends PeerRpc{

    /**
     * 启动服务
     */
    void start();
}
