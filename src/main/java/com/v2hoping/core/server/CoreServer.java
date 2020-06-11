package com.v2hoping.core.server;

/**
 * Created by houping wang on 2020/6/5
 *
 * @author houping wang
 */
public class CoreServer {

    public static void main(String[] args) {
        Server server = new DefaultServer();
        server.start();
    }
}
