package com.v2hoping.core.log;

import com.v2hoping.core.machine.Data;

/**
 * Created by houping wang on 2020/5/13
 * 日志数据，包含状态机中的key-value数据
 * 以及当前索引和任期
 * @author houping wang
 */
public class LogEntry {

    private Long index;

    private Long term;

    private Data data;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
