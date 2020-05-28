package com.v2hoping.core.server;

import com.v2hoping.core.log.DefaultLog;
import com.v2hoping.core.log.Log;

import java.util.Map;

/**
 * Created by houping wang on 2020/5/13
 *
 * @author houping wang
 */
public class DefaultServer implements Server{

    /**
     * 服务器最后一次知道的任期号（初始化为最大索引任期值，持续递增）
     */
    private volatile Long currentTerm;

    /**
     * 在当前获得选票的候选人的 Id(通过心跳维持)
     */
    private volatile String votedFor;

    /**
     * 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号
     */
    private Log log;

    /**
     * 非持久化数据
     */
    private Long commitIndex;

    /**
     * 最后被应到状态机的
     */
    private Long lastApplied;

    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    private Map<String, Long> nextIndex;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    private Map<String, Long> matchIndex;

    /*** 以下定义服务需要值 ***/

    /**
     * 是否已开启
     */
    private volatile boolean stated = false;

    @Override
    public void start() {
        //如果服务已经开启则直接返回，防止重复开启服务
        if(stated) {
           return;
        }
        //获得日志处理器
        log = DefaultLog.getInstance();

    }
}
