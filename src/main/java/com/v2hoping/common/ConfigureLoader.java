package com.v2hoping.common;

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

    private static final Map<String, String> MAP = new HashMap<String, String>();

    static {
        init();
        initDbPath();
    }

    public static String getKey(String key) {
        return MAP.get(key);
    }

    private static void init() {
        MAP.put(SYSTEM_DB_ROOT, "db");
        MAP.put(CLUSTER_NAME, "default");
        MAP.put(CLUSTER_SERVERS, "localhost:8000,localhost:8001,localhost:8002");
        MAP.put(CLUSTER_SELF, "localhost:8000");
    }

    private static void initDbPath() {
        String clusterName = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_NAME);
        String host = ConfigureLoader.getKey(ConfigureLoader.CLUSTER_SELF);
        String system = ConfigureLoader.getKey(ConfigureLoader.SYSTEM_DB_ROOT);
        String port = host.split(":")[1];
        MAP.put(SYSTEM_DB_PATH, system + File.separator + clusterName + File.separator + port);
    }
}
