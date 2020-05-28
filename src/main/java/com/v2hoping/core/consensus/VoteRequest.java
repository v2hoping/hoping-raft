package com.v2hoping.core.consensus;

/**
 * Created by houping wang on 2020/5/9
 * 投票请求
 * @author houping wang
 */
public class VoteRequest {

    /**
     * 候选人的任期号
     */
    private Long term;

    /**
     * 请求选票的候选人ID
     */
    private Long candidateId;

    /**
     * 候选人最后日志条目的索引值
     */
    private Long lastLogIndex;

    /**
     * 候选人最后日志条目的任期号
     */
    private Long lastLogTerm;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(Long lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public Long getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(Long lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }
}
