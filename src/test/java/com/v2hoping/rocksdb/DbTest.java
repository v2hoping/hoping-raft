package com.v2hoping.rocksdb;

import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.Snapshot;

import java.io.File;

/**
 * Created by houping wang on 2020/5/8
 *
 * @author houping wang
 */
public class DbTest {

    public static void main(String[] args) {
        byte[] key1 = "key1".getBytes();
        byte[] value1 = "value1".getBytes();
        //创建目录
        String path = "Users/rocksdb/test";
        new File(path).mkdirs();
        //加载
        RocksDB.loadLibrary();
        // the Options class contains a set of configurable MapDb options
        // that determines the behaviour of the database.
        try (final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            try (final RocksDB db = RocksDB.open(options, "Users/rocksdb/test")) {
                try {
                    final byte[] value = db.get(key1);
                    if (value == null) {  // value == null if key1 does not exist in db.
                        db.put(key1, value1);
                    }
                    System.out.println("输出key1:" + ((value == null) ? null : new String(value)));
                } catch (RocksDBException e) {
                    // error handling
                }
                // do something
            }
        } catch (RocksDBException e) {
            // do some error handling
        }
    }

    @Test
    public void put() {
        byte[] key1 = "key1".getBytes();
        byte[] value2 = "value2".getBytes();
        //创建目录
        String path = "Users/rocksdb/test";
        new File(path).mkdirs();
        //加载
        RocksDB.loadLibrary();
        // the Options class contains a set of configurable MapDb options
        // that determines the behaviour of the database.
        try (final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            try (final RocksDB db = RocksDB.open(options, "Users/rocksdb/test")) {
                try {
                    final byte[] value = db.get(key1);
                    if (value != null) {  // value == null if key1 does not exist in db.
                        db.put(key1, value2);
                        System.out.println("输出key1:" + ((value == null) ? null : new String(value)));
                    }
                } catch (RocksDBException e) {
                    // error handling
                }
                // do something
            }
        } catch (RocksDBException e) {
            // do some error handling
        }
    }

    @Test
    public void get() {
        byte[] key1 = "key1".getBytes();
        byte[] value1 = "value1".getBytes();
        byte[] value2 = "value2".getBytes();
        //创建目录
        String path = "Users/rocksdb/test";
        new File(path).mkdirs();
        //加载
        // the Options class contains a set of configurable MapDb options
        // that determines the behaviour of the database.
        try (final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            try (final RocksDB db = RocksDB.open(options, "Users/rocksdb/test")) {
                try {
                    final byte[] value = db.get(key1);
                    if (value != null) {  // value == null if key1 does not exist in db.
                        System.out.println("输出key1:" + ((value == null) ? null : new String(value)));
                    }
                } catch (RocksDBException e) {
                    // error handling
                }
                // do something
            }
        } catch (RocksDBException e) {
            // do some error handling
        }
    }

    @Test
    public void snapshot() {
        try (final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            try (final RocksDB db = RocksDB.open(options, "Users/rocksdb/test")) {
                Snapshot snapshot = db.getSnapshot();
                long sequenceNumber = snapshot.getSequenceNumber();
                // do something
                long latestSequenceNumber = db.getLatestSequenceNumber();
                System.out.println(latestSequenceNumber);
            }
        } catch (RocksDBException e) {
            // do some error handling
        }
    }
}
