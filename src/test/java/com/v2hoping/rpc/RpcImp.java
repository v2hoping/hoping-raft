package com.v2hoping.rpc;

/**
 * Created by houping wang on 2020/5/8
 *
 * @author houping wang
 */
public class RpcImp implements RpcService{
    public String say(String name) {
        System.out.println("[发送]输出日志");
        return "输出日志";
    }
}
