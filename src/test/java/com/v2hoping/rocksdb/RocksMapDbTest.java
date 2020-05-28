package com.v2hoping.rocksdb;

import com.v2hoping.core.db.MapDb;
import com.v2hoping.core.db.RocksMapDb;
import org.junit.Test;

/**
 * Created by houping wang on 2020/5/12
 *
 * @author houping wang
 */
public class RocksMapDbTest {

    @Test
    public void test() {
        MapDb instance = RocksMapDb.getInstance("Users/rocksdb/test1");
//        instance.put("key110".getBytes(), "value".getBytes());
        MapDb instance1 = RocksMapDb.getInstance("Users/rocksdb/test2");
//        instance1.put("key110".getBytes(), "value1".getBytes());
        //获取
        System.out.println(new String(instance.get("key110".getBytes())));
        System.out.println(new String(instance1.get("key110".getBytes())));
    }
}
