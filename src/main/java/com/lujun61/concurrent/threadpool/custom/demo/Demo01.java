package com.lujun61.concurrent.threadpool.custom.demo;

import com.lujun61.concurrent.threadpool.custom.CustomThreadPool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description 没有拒绝策略
 * @author Jun Lu
 * @date 2023-04-18 19:29:56
 */
@Slf4j
public class Demo01 {

    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(10, 2, 1000, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 5; i++) {
            // i是一直在变化的，不能被匿名内部类直接引用！！！
            String task = "task" + i;
            pool.execute(() -> {
                log.info(task);
            });
        }
    }

}
