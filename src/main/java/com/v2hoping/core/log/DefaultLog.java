package com.v2hoping.core.log;

import com.v2hoping.common.ByteHelper;
import com.v2hoping.common.ConfigureLoader;
import com.v2hoping.common.HopingException;
import com.v2hoping.core.db.MapDb;
import com.v2hoping.core.db.RocksMapDb;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by houping wang on 2020/5/13
 *
 * @author houping wang
 */
public class DefaultLog implements Log{

    /**
     * 获得指定path的数据库连接器，该实例为单例
     */
    private MapDb mapDb;

    private static DefaultLog INSTANCE;

    private static final String LAST_INDEX_KEY = "LAST_INDEX_KEY";

    private static final String LOG = "log";

    private ReentrantLock lock = new ReentrantLock();

    public static DefaultLog getInstance() {
        if(INSTANCE == null) {
            synchronized (DefaultLog.class) {
                if(INSTANCE == null) {
                    INSTANCE = new DefaultLog();
                }
            }
        }
        return INSTANCE;
    }

    private DefaultLog() {
        String dbPath = ConfigureLoader.getKey(ConfigureLoader.SYSTEM_DB_PATH);
        mapDb = RocksMapDb.getInstance(dbPath + File.separator + LOG);
    }

    @Override
    public void put(LogEntry logEntry) {
        try{
            lock.lock();
            logEntry.setIndex(getLastIndex() + 1);
            mapDb.put(ByteHelper.serialize(logEntry.getIndex()), ByteHelper.serialize(logEntry));
            mapDb.put(ByteHelper.serialize(LAST_INDEX_KEY), ByteHelper.serialize(logEntry.getIndex()));
        }catch (Exception e) {
            throw new HopingException("[日志处理器]put异常。" + e.getMessage(), e);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public LogEntry get(Long index) {
        byte[] bytes = mapDb.get(ByteHelper.serialize(index));
        if(bytes == null || bytes.length == 0) {
            return null;
        }
        return ByteHelper.deserialize(bytes, LogEntry.class);
    }

    @Override
    public void delFromStartIndex(Long index) {
        int count = 0;
        try{
            lock.lock();
            Long lastIndex = this.getLastIndex();
            for(long i = index; i < lastIndex; i ++) {
                mapDb.del(ByteHelper.serialize(index));
                count ++;
            }
            if(count != 0) {
                mapDb.put(ByteHelper.serialize(LAST_INDEX_KEY), ByteHelper.serialize(lastIndex - count));
            }
        }catch (Exception e) {
            throw new HopingException("[日志处理器]del异常。" + e.getMessage(), e);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public LogEntry getLastLog() {
        Long lastIndex = this.getLastIndex();
        return this.get(lastIndex);
    }

    @Override
    public Long getLastIndex() {
        byte[] bytes = mapDb.get(ByteHelper.serialize(LAST_INDEX_KEY));
        if(bytes == null || bytes.length == 0) {
            return null;
        }
        return ByteHelper.deserialize(bytes, Long.class);
    }
}
