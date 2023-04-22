package com.lujun61.concurrent.status;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示 java 线程的 6 种状态(NEW, RUNNABLE, TERMINATED, BLOCKED, WAITING, TIMED_WAITING)
 */
@Slf4j
public class ThreadSixStatus {

    public static void main(String[] args) {

        // NEW
        Thread t1 = new Thread(() -> {
            log.info("NEW 状态");
        }, "t1");

        // RUNNABLE
        Thread t2 = new Thread(() -> {
            while (true) {

            }
        }, "t2");

        t2.start();

        // TERMINATED
        Thread t3 = new Thread(() -> {
            log.info("running");
        }, "t3");
        t3.start();

        // TIMED_WAITING
        Thread t4 = new Thread(() -> {
            synchronized (ThreadSixStatus.class) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");
        t4.start();

        // WAITING
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");
        t5.start();

        // BLOCKED
        Thread t6 = new Thread(() -> {
            synchronized (ThreadSixStatus.class) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t6");
        t6.start();

        // 主线程休眠 1 秒, 目的是为了等待 t3 线程执行完
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("t1 线程状态: {}", t1.getState());
        log.info("t2 线程状态: {}", t2.getState());
        log.info("t3 线程状态: {}", t3.getState());
        log.info("t4 线程状态: {}", t4.getState());
        log.info("t5 线程状态: {}", t5.getState());
        log.info("t6 线程状态: {}", t6.getState());
    }
}
