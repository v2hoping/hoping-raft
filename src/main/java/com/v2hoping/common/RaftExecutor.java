package com.v2hoping.common;

import java.util.concurrent.*;

/**
 * Created by houping wang on 2020/6/12
 *
 * @author houping wang
 */
public class RaftExecutor {

    private static final int CPU = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor ELECTION_EXECUTOR = new ThreadPoolExecutor(CPU * 4, CPU * 4, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1024), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Election" + System.currentTimeMillis());
            thread.setDaemon(true);
            return thread;
        }
    });

    public static void submitElection(Runnable task) {
        ELECTION_EXECUTOR.submit(task);
    }

    public static <T> Future<T> submitElection(Callable<T> task) {
        return ELECTION_EXECUTOR.submit(task);
    }
}
