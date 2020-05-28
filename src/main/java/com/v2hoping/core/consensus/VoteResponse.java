package com.v2hoping.core.consensus;

/**
 * Created by houping wang on 2020/5/9
 * 投票响应
 * @author houping wang
 */
public class VoteResponse {

    /**
     * 当前任期号、以便于候选人去更新自己的任期号
     */
    private Long term;

    /**
     * 候选人赢的了此张选票时为真
     */
    private Boolean voteGranted;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Boolean getVoteGranted() {
        return voteGranted;
    }

    public void setVoteGranted(Boolean voteGranted) {
        this.voteGranted = voteGranted;
    }
}
