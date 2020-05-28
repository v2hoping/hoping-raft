package com.v2hoping.core.machine;

/**
 * Created by houping wang on 2020/5/9
 * 状态机
 * 使用RocksMapDb存储在key-value数据库中
 * @author houping wang
 */
public interface StateMachine {

    void put(String key, String value);

    String get(String key);

    void del(String key);
}
