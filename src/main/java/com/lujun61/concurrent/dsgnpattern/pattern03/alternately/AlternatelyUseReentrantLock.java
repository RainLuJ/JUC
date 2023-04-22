package com.lujun61.concurrent.dsgnpattern.pattern03.alternately;

import com.lujun61.concurrent.util.JucUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AlternatelyUseReentrantLock {

    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(3);

        // 三间休息室
        Condition c1 = awaitSignal.newCondition();
        Condition c2 = awaitSignal.newCondition();
        Condition c3 = awaitSignal.newCondition();

        Thread t1 = new Thread(() -> {
            awaitSignal.print("a", c1, c2);
        }, "t1");

        Thread t2 = new Thread(() -> {
            awaitSignal.print("b", c2, c3);
        }, "t2");

        Thread t3 = new Thread(() -> {
            awaitSignal.print("c", c3, c1);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        // 确保所有线程都进入了“休息室”（状态变量），再从c1开始唤醒
        JucUtils.sleep(1000);

        awaitSignal.lock();
        try {
            c1.signal();
        } finally {
            awaitSignal.unlock();
        }
    }

    static class AwaitSignal extends ReentrantLock {
        private int loopnum;

        public AwaitSignal() {
        }

        public AwaitSignal(int loopnum) {
            this.loopnum = loopnum;
        }

        public void print(String str, Condition current, Condition next) {
            for (int i = 0; i < loopnum; i++) {
                lock();

                try {
                    current.await();
                    System.out.print(str);
                    next.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    unlock();
                }
            }
        }
    }

}
