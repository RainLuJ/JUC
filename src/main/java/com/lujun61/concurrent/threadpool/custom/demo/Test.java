package com.lujun61.concurrent.threadpool.custom.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    public static void main(String[] args) {
        MyThread t1 = new MyThread(() -> {
            log.info("t1");
        }, "t1");

        t1.start();

        log.info("main");
    }

}

class MyThread extends Thread {
    Runnable task;

    public MyThread() {
    }

    public MyThread(Runnable task, String name) {
        this.task = task;
        this.setName(name);
    }

    @Override
    public void run() {
        task.run();
    }
}
