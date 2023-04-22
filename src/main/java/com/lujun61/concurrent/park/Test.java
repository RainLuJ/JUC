package com.lujun61.concurrent.park;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            JucUtils.sleep(3000);
            log.info("t1 end");
        }, "t1");
        t1.start();

        t1.join();
        //t1.interrupt();
        Thread main = Thread.currentThread();
        main.interrupt();
        log.info("main start~");
    }

}
