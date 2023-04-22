package com.lujun61.concurrent.threadpool.api;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CachedThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        // 1. 刚开始池中没有线程，十个任务，由于sleep了1秒，则有足够的时间创建十个线程
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                log.info("任务被线程【{}】执行", Thread.currentThread().getName());

                JucUtils.sleep(1000);
            });
        }

        JucUtils.sleep(3000);

        // 2. 由于没有超过60秒的最大空闲时间，所以再有任务过来时，会重用一开始所创建的线程
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                log.info("任务被线程【{}】执行", Thread.currentThread().getName());

                JucUtils.sleep(1000);
            });
        }
    }
}
