package com.v2hoping.common;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.sql.Time;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by houping wang on 2020/5/21
 * 时间轮算法，用于高效触发定时任务
 * @author houping wang
 */
public class TimingWheel {

    public static void main(String[] args) throws InterruptedException {
        long duration = TimeUnit.MILLISECONDS.toNanos(1);

        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "线程" + System.currentTimeMillis());
            }
        }, 1, TimeUnit.MILLISECONDS, 1024);
        System.out.println(System.currentTimeMillis());
        hashedWheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {

            }
        }, 10000, TimeUnit.MILLISECONDS);
    }

}
