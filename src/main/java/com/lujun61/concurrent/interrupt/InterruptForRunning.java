package com.lujun61.concurrent.interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * @description 打断正常运行
 * @author Jun Lu
 * @date 2023-03-30 11:00:22
 */
@Slf4j
public class InterruptForRunning {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                Thread currentThread = Thread.currentThread();
                if(currentThread.isInterrupted()) {
                    log.debug(currentThread.getName() + "被interrupt了！");
                    break;
                }
            }
        }, "t1");

        t1.start();
        t1.interrupt();

        log.info("t1.isInterrupted() = " + t1.isInterrupted());
    }
}
