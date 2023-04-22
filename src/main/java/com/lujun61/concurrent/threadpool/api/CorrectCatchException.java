package com.lujun61.concurrent.threadpool.api;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Jun Lu
 * @description 正确处理异常
 * @date 2023-04-20 19:40:24
 */
@Slf4j
public class CorrectCatchException {
    static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // test01();

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Boolean> f = pool.submit(() -> {
            log.debug("task1");
            int i = 1 / 0;
            return true;
        });
        log.debug("result:{}", f.get());
    }

    private static void test01() {
        threadPool.execute(() -> {
            log.info("0：{}", Thread.currentThread().getName());

            try {
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 异常未被捕获，程序异常终止
            log.info("Running");
        });

        threadPool.submit(() -> {
            log.info("1：{}", Thread.currentThread().getName());

            try {
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.info("Running");
        });

        threadPool.execute(() -> {
            log.info("2：{}", Thread.currentThread().getName());
        });

        threadPool.execute(() -> {
            log.info("3：{}", Thread.currentThread().getName());
        });
    }

}
