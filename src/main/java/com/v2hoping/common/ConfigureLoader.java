package com.v2hoping.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.v2hoping.core.db.RocksMapDb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by houping wang on 2020/5/12
 *
 * @author houping wang
 */
public class ConfigureLoader {

    public static final String SYSTEM_DB_ROOT = "system.db.root";

    public static final String SYSTEM_DB_PATH = "system.db.path";

    public static final String CLUSTER_NAME = "cluster.name";

    public static final String CLUSTER_SERVERS = "cluster.servers";

    public static final String CLUSTER_SELF = "cluster.self";

    public static final String CLUSTER_RPC_PORT = "cluster.rpc.port";

    public static final String CLUSTER_ELECTION_OUT_TIME_MIN = "cluster.election.out.time.min";

    public static final String CLUSTER_ELECTION_OUT_TIME_MAX = "cluster.election.out.time.max";

    public static final Map<String, String> MAP = new HashMap<String, String>();

    static {
        init();
    }

    public static String getKey(String key) {
        if(SYSTEM_DB_PATH.equals(key)) {
            initDbPath();
        }
        return MAP.get(key);
    }

    public static <T> T getTKey(String key, TypeReference<T> reference) {
        return JSON.parseObject(getKey(key), reference);
    }

    private static void init() {
        MAP.put(SYSTEM_DB_ROOT, "db");
        MAP.put(CLUSTER_NAME, "default");
        MAP.put(CLUSTER_SERVERS, "localhost:8000,localhost:8001,localhost:8002");
        MAP.put(CLUSTER_SELF, "localhost:8000");
        MAP.put(CLUSTER_RPC_PORT, "12200");
        MAP.put(CLUSTER_ELECTION_OUT_TIME_MIN, "");
    }

    private static void initDbPath() {
        String clusterName = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_NAME);
        String host = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_SELF);
        String system = ConfigureLoader.getKey(ConfigureLoader.SYSTEM_DB_ROOT);
        String port = host.split(":")[1];
        MAP.put(SYSTEM_DB_PATH, system + File.separator + clusterName + File.separator + port);
    }
}
