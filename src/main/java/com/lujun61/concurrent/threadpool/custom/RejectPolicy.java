package com.lujun61.concurrent.threadpool.custom;

@FunctionalInterface
public interface RejectPolicy<T> {
    /**
     * @param queue 当前所操作的阻塞队列
     * @param task 当前所执行的任务
     * @description 拒绝策略
     * @author Jun Lu
     * @date 2023-04-18 18:43:18
     */
    void reject(BlockingQueue<T> queue, T task);
}
