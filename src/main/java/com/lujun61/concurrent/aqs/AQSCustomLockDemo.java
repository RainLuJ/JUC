package com.lujun61.concurrent.aqs;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AQSCustomLockDemo {

    public static void main(String[] args) {

        AQSCustomLock myLock = new AQSCustomLock();
        new Thread(() -> {
            myLock.lock();
            log.info("lock ... ");
            // 测试是否可重入
            myLock.lock();
            try {
                log.info("starting ... ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("unlock ... ");
                myLock.unlock();
            }
        }, "t1").start();

    }

}
