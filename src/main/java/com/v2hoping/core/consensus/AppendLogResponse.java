package com.v2hoping.core.consensus;

/**
 * Created by houping wang on 2020/5/12
 *
 * @author houping wang
 */
public class AppendLogResponse {

    /**
     * 当前的任期号，用于领导人更新自己
     */
    private Long term;

    /**
     * 跟随者包含了匹配上prevLogIndex和prevLogTerm的日志时为真
     */
    private Boolean success;

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
