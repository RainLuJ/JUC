package com.lujun61.concurrent.park;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Test03 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                log.info("park 1");
                LockSupport.park();
                log.info("park 2");
            }
        }, "t1");


        t1.start();

        log.info("unpark 1");
        LockSupport.unpark(t1);
        log.info("unpark 2");

        JucUtils.sleep(3000);
        log.info("unpark 3");
        LockSupport.unpark(t1);
        log.info("unpark 4");

    }

}
