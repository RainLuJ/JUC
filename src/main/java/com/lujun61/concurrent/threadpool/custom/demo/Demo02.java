package com.lujun61.concurrent.threadpool.custom.demo;

import com.lujun61.concurrent.threadpool.custom.CustomThreadPool;
import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description 自定义拒绝策略
 * @author Jun Lu
 * @date 2023-04-18 19:29:56
 */
@Slf4j
public class Demo02 {

    public static void main(String[] args) {

        CustomThreadPool pool = new CustomThreadPool(1, 1, 1000, TimeUnit.MILLISECONDS, (queue, task) -> {
            /* 策略演绎 */

            // 1、死等
            //queue.put(task);

            // 2、超时等待
            //queue.put(task, 500, TimeUnit.MILLISECONDS);

            // 3、调用者放弃执行任务
            //log.info("放弃执行任务：【{}】", task.hashCode());

            // 4、让调用者抛出异常
            //throw new RuntimeException("任务执行失败" + task.hashCode());

            // 5、让调用者自己执行任务
            //task.run();
        });


        for (int i = 0; i < 5; i++) {
            // i是一直在变化的，不能被匿名内部类直接引用！！！
            String task = "task" + i;
            pool.execute(() -> {
                JucUtils.sleep(1000);
                log.info(task);
            });
        }

    }

}
