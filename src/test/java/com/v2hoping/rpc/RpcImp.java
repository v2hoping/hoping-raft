package com.v2hoping.rpc;

/**
 * Created by houping wang on 2020/5/8
 *
 * @author houping wang
 */
public class RpcImp implements RpcService{
    public String say(String name) {
        try {
            System.out.println("[发送]输出日志");
            Thread.sleep(2000);
            System.out.println("[发送]输出日志完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "输出日志";
    }
}
