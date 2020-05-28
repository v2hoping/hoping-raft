package com.v2hoping.main;

/**
 * Created by houping wang on 2020/5/14
 *
 * @author houping wang
 */
public class Main {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("输出1");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("输出");
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("主线程");
    }
}
