package com.lujun61.concurrent.dsgnpattern.pattern03.regular;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegularUseWaitNotify {

    private static final Object lock = new Object();
    private static boolean isT1 = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.info("enter t1");
            while (!isT1) {
                synchronized (lock) {
                    try {
                        log.info("t1 wait");
                        lock.wait();
                        log.info("t1 wait end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            log.info("t1 complete!");
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.info("t2 enter");
                isT1 = true;
                log.info("t2 complete!");
                log.info("t1 wake up!!!");
                lock.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}
