package com.v2hoping.executor;

import com.v2hoping.common.RaftExecutor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by houping wang on 2020/6/12
 *
 * @author houping wang
 */
public class ExecutorTest {

    @Test
    public void test() {
        RaftExecutor.submitElection(new Runnable() {
            @Override
            public void run() {
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.info("输出");
                System.out.println("执行");
            }
        });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
