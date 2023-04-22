package com.lujun61.concurrent.threadpool.api;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class NewWorkStealingPoolDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newWorkStealingPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threadPool.execute(() -> {
                log.info(index + "被执行");
            });
        }

        // 确保所有任务执行完成之后，主线程才能终止运行
        while (!threadPool.isTerminated()) {
        }
    }
}
