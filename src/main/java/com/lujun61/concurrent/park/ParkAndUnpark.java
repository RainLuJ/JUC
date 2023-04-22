package com.lujun61.concurrent.park;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ParkAndUnpark {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            JucUtils.sleep(1000);
            log.info("park before");
            LockSupport.park();
            log.info("park after");
        }, "t1");
        t1.start();

        JucUtils.sleep(1500);
        log.info("unpark before");
        LockSupport.unpark(t1);
        log.info("unpark after");

    }

}
