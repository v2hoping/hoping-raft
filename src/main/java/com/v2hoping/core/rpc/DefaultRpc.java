package com.v2hoping.core.rpc;

import com.v2hoping.core.consensus.AppendLogRequest;
import com.v2hoping.core.consensus.AppendLogResponse;
import com.v2hoping.core.consensus.VoteRequest;
import com.v2hoping.core.consensus.VoteResponse;

/**
 * Created by houping wang on 2020/5/22
 * 远程调用
 * @author houping wang
 */
public class DefaultRpc implements Rpc{
    @Override
    public VoteResponse vote(VoteRequest voteRequest) {
        return null;
    }

    @Override
    public AppendLogResponse appendLog(AppendLogRequest appendLogRequest) {
        return null;
    }
}
