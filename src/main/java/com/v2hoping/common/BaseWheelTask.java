package com.v2hoping.common;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

/**
 * Created by houping wang on 2020/5/22
 *
 * @author houping wang
 */
public abstract class BaseWheelTask implements TimerTask, Runnable {

    private Timeout timeout;

    public Timeout getTimeout() {
        return timeout;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        this.timeout = timeout;
        WheelExecutor.THREAD_POOL_EXECUTOR.submit(this);
    }
}
