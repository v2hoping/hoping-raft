package com.v2hoping.core.db;

/**
 * Created by houping wang on 2020/5/12
 * key-value数据库
 * @author houping wang
 */
public interface MapDb {

    /**
     * 根据key获得value的字节数据
     * @param key key
     * @return value
     */
    byte[] get(byte[] key);

    /**
     * 根据key删除字节数据
     * @param key key
     */
    void del(byte[] key);

    /**
     * 格局key设置字节数据
     * @param key key
     * @param value value
     */
    void put(byte[] key, byte[] value);

    /**
     * 关闭DB
     */
    void closeDb();
}
