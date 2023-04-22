package com.lujun61.concurrent.cas.cas01;

import java.util.ArrayList;
import java.util.List;

public interface Account {

    // 获取金额的方法
    Integer getBalance();

    // 取款的方法
    void withdraw(Integer amount);

    static void demo(Account account) {
        List<Thread> list = new ArrayList<>();

        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            list.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }

        // 启动所有线程
        for (Thread thread : list) {
            thread.start();
        }

        // 等待所有线程执行完
        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.nanoTime();

        System.out.println(account.getBalance()
                + " 花费时间: " + (end - start) / 1000_000 + " ms");
    }

}
