package com.v2hoping.core.consensus;

/**
 * Created by houping wang on 2020/5/12
 *
 * @author houping wang
 */
public class DefaultConsensusModule implements ConsensusModule{

    @Override
    public VoteResponse vote(VoteRequest voteRequest) {
        return null;
    }

    @Override
    public AppendLogResponse appendLog(AppendLogRequest appendLogRequest) {
        return null;
    }
}
