package com.v2hoping.core.consensus;

/**
 * Created by houping wang on 2020/5/9
 * 一致性协调接口
 * @author houping wang
 */
public interface ConsensusModule {

    /**
     * 请求投票RPC
     * @param voteRequest 投票请求
     * @return 投票响应
     */
    VoteResponse vote(VoteRequest voteRequest);

    /**
     * 追加日志RPC
     * @param appendLogRequest 追加日志请求
     * @return 追加日志响应
     */
    AppendLogResponse appendLog(AppendLogRequest appendLogRequest);
}
