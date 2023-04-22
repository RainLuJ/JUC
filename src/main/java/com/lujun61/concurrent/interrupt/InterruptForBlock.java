package com.lujun61.concurrent.interrupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description 打断阻塞
 * @author Jun Lu
 * @date 2023-03-30 11:00:06
 */
@Slf4j
public class InterruptForBlock {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            try {
                log.debug("sleep");
                TimeUnit.SECONDS.sleep(20L);
                log.debug("wake up");
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("catch before = " + currentThread.isInterrupted());
                currentThread.interrupt();
                log.info("catch after = " + currentThread.isInterrupted());
            }
        }, "t1");

        t1.start();
        TimeUnit.SECONDS.sleep(1L);
        log.info("t1---interrupt");
        t1.interrupt();
        log.info("t1.isInterrupted() = " + t1.isInterrupted());
    }
}
