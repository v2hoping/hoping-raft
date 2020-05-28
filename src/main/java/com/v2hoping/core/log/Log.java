package com.v2hoping.core.log;

/**
 * Created by houping wang on 2020/5/13
 * 日志管理
 * 以index作为主键进行操作
 * @author houping wang
 */
public interface Log {

    /**
     * 存储日志
     * @param logEntry 日志实体
     */
    void put(LogEntry logEntry);

    /**
     * 获得index索引日志
     * @param index 索引
     */
    LogEntry get(Long index);

    /**
     * 删除从index处至末位的索引
     * @param index 开始索引
     */
    void delFromStartIndex(Long index);

    /**
     * 获得最大的索引日志
     * @return LogEntry 日志实体
     */
    LogEntry getLastLog();

    /**
     * 获得最大的索引
     * @return Long 日志索引
     */
    Long getLastIndex();
}
