package com.lujun61.concurrent.dsgnpattern.pattern03.regular;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class RegularUseParkUnpark {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("t1 complete!");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.info("t2 complete!");
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        t2.start();
    }

}
