package com.lujun61.concurrent.threadpool.custom;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomThreadPool {

    // 1、阻塞式任务队列（线程安全的）：每个Runnable，就是一个待执行的任务
    private BlockingQueue<Runnable> taskQueue;

    // 2、线程集合（非线程安全的）：存放线程池中的多个线程对象
    private final HashSet<Worker> workers = new HashSet<>();

    // 3、核心线程数：最大所能支持的同时运行的线程数
    private int coreSize;

    // 4、获取任务的超时时间
    private long timeout;
    // 时间单元
    private TimeUnit timeUnit;

    // 5、拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public CustomThreadPool() {
    }

    /**
     * 没有自定义拒绝策略
     */
    public CustomThreadPool(int queueCapcity, int coreSize, long timeout, TimeUnit timeUnit) {
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * 可以自定义拒绝策略
     */
    public CustomThreadPool(int queueCapcity, int coreSize, long timeout, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    // Worker本质上就是一个线程对象，用来执行任务
    class Worker extends Thread {
        private Runnable task;

        public Worker() {
        }

        public Worker(Runnable task) {
            this.task = task;
        }

        public Worker(Runnable task, String name) {
            this.task = task;
            this.setName(name);
        }

        @Override
        public void run() {
            /*
                执行任务：
                    · 当task不为空，则执行task
                    · 当某一个task执行完毕，就接着从task队列中获取任务并执行
             */
            while (task != null || (task = taskQueue.take(timeout, timeUnit)) != null) {
                try {
                    log.debug("任务【{}】执行中……", task.hashCode());
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 执行完task后，则置空这个task
                    task = null;
                }
            }

            synchronized (workers) {
                log.debug("worker被移除：{}", this);
                workers.remove(this);
            }
        }
    }

    // 执行任务
    public void execute(Runnable task) {
        /*
            当 正在被线程执行的任务数 没有超过 coreSize 时，直接将任务交给 Worker对象 执行；
            当 正在被线程执行的任务数 超过 coreSize 时，则加入至任务队列暂存。
         */
        // 保证 workers 集合的线程安全
        synchronized (workers) {
            if (workers.size() < coreSize) {
                // 新建一个[Worker]线程对象，用来执行任务
                Worker worker = new Worker(task);
                log.debug("新建一个worker：{}", worker);
                workers.add(worker);
                worker.start();
            } else {
                log.debug("任务【{}】存放至阻塞队列", task.hashCode());
                // taskQueue.put(task);  // 死等

                /*      当遇到队列满而产生阻塞时，有不同策略的解决方式。
                        但具体使用什么策略，应该交由调用者控制，而不能写死在代码中。
                    拒绝策略：
                        1) 死等
                        2) 带超时等待
                        3) 让调用者放弃任务等待
                        4) 让调用者抛出异常
                        5) 让调用者自己执行任务
                        ……
                 */
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }
}

