package com.lujun61.concurrent.park;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Test01 {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.lock();

            log.info("wait before~");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("wait after~");

            lock.unlock();
        }, "t1");

        lock.lock();

        JucUtils.sleep(3000);
        lock.notifyAll();

        lock.unlock();

    }

}
