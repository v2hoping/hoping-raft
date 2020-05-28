package com.v2hoping.core.db;

import com.v2hoping.common.HopingException;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by houping wang on 2020/5/12
 * 统一管理RocksDB
 * 单纯包装RocksDB接口，只暴露我们需要的接口给上层
 *
 * @author houping wang
 */
public class RocksMapDb implements MapDb {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksMapDb.class);

    private RocksDB db;

    private volatile static Map<String, RocksMapDb> MAP = new ConcurrentHashMap<>(2);

    private RocksMapDb(String path) {
        try {
            boolean mkdirs = new File(path).mkdirs();
            if (mkdirs) {
                LOGGER.info("hoping-raft>>>>>>rocksdb数据库[" + path + "]目录创建");
            }
            db = RocksDB.open(new Options().setCreateIfMissing(true), path);
            LOGGER.info("hoping-raft>>>>>>rocksdb数据库连接成功");
        } catch (RocksDBException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static MapDb getInstance(String path) {
        RocksMapDb instance = MAP.get(path);
        if (instance == null) {
            synchronized (RocksMapDb.class) {
                instance = MAP.get(path);
                if (instance == null) {
                    instance = new RocksMapDb(path);
                    MAP.put(path, instance);
                }
            }
        }
        return instance;
    }

    public static void closeAnything() {
        try {
            Collection<RocksMapDb> values = MAP.values();
            for (RocksMapDb rocksMapDb : values) {
                rocksMapDb.closeDb();
            }
        } catch (Exception e) {
            throw new HopingException(e);
        }

    }

    @Override
    public byte[] get(byte[] key) {
        try {
            return db.get(key);
        } catch (RocksDBException e) {
            throw new HopingException(e);
        }
    }

    @Override
    public void del(byte[] key) {
        try {
            db.delete(key);
        } catch (RocksDBException e) {
            throw new HopingException(e);
        }
    }

    @Override
    public void put(byte[] key, byte[] value) {
        try {
            db.put(key, value);
        } catch (RocksDBException e) {
            throw new HopingException(e);
        }
    }

    @Override
    public void closeDb() {
        db.close();
    }
}
