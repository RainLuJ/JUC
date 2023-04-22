package com.lujun61.concurrent.park;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Test04 {
    
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (Test04.class) {
                LockSupport.park();
            }
            
            log.info("t1 run~");
        }, "t1");
        t1.start();

        JucUtils.sleep(100);

        Thread t2 = new Thread(() -> {
            synchronized (Test04.class) {
                log.info("t2 run~");
            }
        }, "t2");
        t2.start();

        log.info("main run~");
    }

}
