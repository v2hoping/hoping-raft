package com.v2hoping.core.consensus;

import com.v2hoping.core.log.LogEntry;

/**
 * Created by houping wang on 2020/5/9
 * 追加日志请求
 * @author houping wang
 */
public class AppendLogRequest {

    /**
     * 领导人的任期号
     */
    private Long term;

    /**
     * 领导人的ID，以便于跟随者重定向请求
     */
    private Long leaderId;

    /**
     * 新的日志条目紧随之前的索引值
     */
    private Long prevLongIndex;

    /**
     * prevLongIndex条目的任期号
     */
    private Long prevLogTerm;

    /**
     * 准备存储的日志条目（表示心跳时为空；一次性发送多个是为了提高效率
     */
    private LogEntry[] entries;

    /**
     * 领导人已经提交的日志的索引值
     */
    private Long leaderCommit;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Long getPrevLongIndex() {
        return prevLongIndex;
    }

    public void setPrevLongIndex(Long prevLongIndex) {
        this.prevLongIndex = prevLongIndex;
    }

    public Long getPrevLogTerm() {
        return prevLogTerm;
    }

    public void setPrevLogTerm(Long prevLogTerm) {
        this.prevLogTerm = prevLogTerm;
    }

    public LogEntry[] getEntries() {
        return entries;
    }

    public void setEntries(LogEntry[] entries) {
        this.entries = entries;
    }

    public Long getLeaderCommit() {
        return leaderCommit;
    }

    public void setLeaderCommit(Long leaderCommit) {
        this.leaderCommit = leaderCommit;
    }
}
