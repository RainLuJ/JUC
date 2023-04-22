package com.lujun61.concurrent.threadpool.api;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class FixedThreadPoolDemo {

    public static void main(String[] args) {

        log.info("主线程启动");
        // 1.创建1个有2个线程的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Runnable runnable = () -> {
            JucUtils.sleep(2000);
            log.info("任务被线程【{}】执行", Thread.currentThread().getName());
        };

        // 2.线程池执行任务。添加4个任务，每次执行2个任务。
        // 当任务还未执行完毕，此时又有任务需要被执行，则会先将其加入阻塞队列。
        threadPool.execute(runnable);
        threadPool.execute(runnable);
        threadPool.execute(runnable);
        threadPool.execute(runnable);

        log.info("主线程结束");
        
    }

}
