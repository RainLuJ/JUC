package com.lujun61.concurrent.dsgnpattern.pattern01;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jun Lu
 * @description 并发编程设计模式之两阶段终止设计模式【使用volatile实现】
 * @date 2023-04-14 08:09:54
 */
@Slf4j
public class TwoParseTerminationUseVolatile {

    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        monitor.start();
        JucUtils.sleep(3500);
        monitor.stop();
    }

    static class Monitor {

        Thread monitor;
        // 设置停止标记，用于判断是否线程是否被终止了
        private volatile boolean stop = false;

        /**
         * 启动监控器线程
         */
        public void start() {
            // 设置线控器线程，用于监控线程状态
            monitor = new Thread(() -> {
                // 开始不停的监控
                while (true) {
                    if (stop) {
                        System.out.println("处理后续任务");
                        break;
                    }
                    System.out.println("监控器运行中...");
                    try {
                        // 线程休眠
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("被打断了");
                    }
                }
            });
            monitor.start();
        }

        /**
         * 用于停止监控器线程
         */
        public void stop() {
            // 修改标记
            stop = true;
            // 打断线程：目的是尽早结束线程。因为在打断线程时，线程可能正在sleep
            monitor.interrupt();
        }
    }

}
