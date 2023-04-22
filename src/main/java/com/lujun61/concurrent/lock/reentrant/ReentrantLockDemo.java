package com.lujun61.concurrent.lock.reentrant;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockDemo {

    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();

            log.info("t1");
            JucUtils.sleep(5000);

            lock.unlock();
        }, "t1");
        t1.start();

        JucUtils.sleep(500);
        lock.lock();
        log.info("main");

        lock.unlock();
    }

}
