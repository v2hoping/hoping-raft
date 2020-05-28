package com.v2hoping.core.machine;

import com.v2hoping.common.ByteHelper;
import com.v2hoping.common.ConfigureLoader;
import com.v2hoping.common.HopingException;
import com.v2hoping.core.db.MapDb;
import com.v2hoping.core.db.RocksMapDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by houping wang on 2020/5/14
 *
 * @author houping wang
 */
public class DefaultStateMachine implements StateMachine{

    private static final String STATE = "state";

    private static DefaultStateMachine INSTANCE;

    /**
     * 获得指定path的数据库连接器，该实例为单例
     */
    private MapDb mapDb;

    private ReentrantLock lock = new ReentrantLock();

    private DefaultStateMachine() {
        String dbPath = ConfigureLoader.getKey(ConfigureLoader.SYSTEM_DB_PATH);
        mapDb = RocksMapDb.getInstance(dbPath + File.separator + STATE);
    }

    public static StateMachine getInstance() {
        if(INSTANCE == null) {
            synchronized (DefaultStateMachine.class) {
                if(INSTANCE == null) {
                    INSTANCE = new DefaultStateMachine();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void put(String key, String value) {
        try{
            lock.lock();
            mapDb.put(ByteHelper.serialize(key), ByteHelper.serialize(value));
        }catch (Exception e) {
            throw new HopingException("[状态机]put异常。" + e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public String get(String key) {
        byte[] bytes = mapDb.get(ByteHelper.serialize(key));
        if(bytes == null || bytes.length == 0) {
            return null;
        }
        return ByteHelper.deserialize(bytes, String.class);
    }

    @Override
    public void del(String key) {
        try{
            lock.lock();
            mapDb.del(ByteHelper.serialize(key));
        }catch (Exception e) {
            throw new HopingException("[状态机]put异常。" + e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
