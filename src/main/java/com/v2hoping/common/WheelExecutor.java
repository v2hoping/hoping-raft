package com.v2hoping.common;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by houping wang on 2020/5/22
 * 处理时间轮任务的线程池，将同步执行的时间轮任务
 *
 * @author houping wang
 */
public class WheelExecutor {

    private static final int CPU = Runtime.getRuntime().availableProcessors();

    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CPU * 2, 10,
            0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(16), new ThreadFactory() {

        private AtomicLong atomicLong = new AtomicLong(0);

        @Override
        public Thread newThread(Runnable r) {
            long index = atomicLong.incrementAndGet();
            return new Thread(r, "WhellTask-" + index);
        }
    });

}
