package com.lujun61.concurrent.threadpool.api;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledThreadPoolDemo {
    public static void main(String[] args) {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);

        // 1. 一次性的任务：执行完后不再执行
        log.info("添加任务，时间：" + new Date());
        threadPool.schedule(() -> {
            log.info("任务1被执行，时间：" + new Date());
        }, 2, TimeUnit.SECONDS);

        // 2. 周期性任务
        threadPool.scheduleAtFixedRate(() -> {
            log.info("任务2被执行，时间：" + new Date());
            /*
                - initialDelay：任务第一次延迟执行的时间
                - period：从`A线程开始执行`到`B线程开始执行`之间的时间间隔
             */
        }, 2, 2, TimeUnit.SECONDS);

        // 3. 周期性任务
        threadPool.scheduleWithFixedDelay(() -> {
            log.info("任务3被执行，时间：" + new Date());
            /*
                - initialDelay：任务第一次延迟执行的时间
                - delay：从`A线程结束`到`B线程开始执行`之间的时间间隔
             */
        }, 2, 2, TimeUnit.SECONDS);
    }
}
