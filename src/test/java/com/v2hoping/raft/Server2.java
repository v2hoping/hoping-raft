package com.v2hoping.raft;

import com.v2hoping.common.ConfigureLoader;
import com.v2hoping.core.server.DefaultServer;
import com.v2hoping.core.server.Server;

/**
 * Created by houping wang on 2020/6/11
 *
 * @author houping wang
 */
public class Server2 {

    public static void main(String[] args) {
        ConfigureLoader.MAP.put(ConfigureLoader.CLUSTER_SELF, "localhost:8001");
        Server server = new DefaultServer();
        server.start();
    }
}
