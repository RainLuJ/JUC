package com.lujun61.concurrent.dsgnpattern.pattern01;

import lombok.extern.slf4j.Slf4j;

/**
 * @description 并发编程设计模式之两阶段终止设计模式【使用interrupt实现】
 * @author Jun Lu
 * @date 2023-03-30 11:09:29
 */
@Slf4j
public class TwoParseTerminationUseInterrupt {

    public static void main(String[] args) throws InterruptedException {
        TwoParseTerminationUseInterrupt tpt = new TwoParseTerminationUseInterrupt();
        tpt.start();
        Thread.sleep(3500);
        tpt.stop();
    }

    private Thread monitor;

    // 启动线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread thread = Thread.currentThread();
                if(thread.isInterrupted()) { // 调用 isInterrupted 不会清除标记
                    log.info("料理后事 ...");
                    break;
                } else {
                    try {
                        Thread.sleep(1000);
                        log.info("执行监控的功能 ...");
                    } catch (InterruptedException e) {
                        log.info("设置打断标记 ...");
                        thread.interrupt();
                        e.printStackTrace();
                    }
                }
            }
        }, "monitor");
        monitor.start();
    }

    // 终止线程
    public void stop() {
        monitor.interrupt();
    }

}
