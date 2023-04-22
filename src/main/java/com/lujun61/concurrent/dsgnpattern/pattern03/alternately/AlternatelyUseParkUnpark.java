package com.lujun61.concurrent.dsgnpattern.pattern03.alternately;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class AlternatelyUseParkUnpark {

    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {
        final ParkUnpark parkUnpark = new ParkUnpark(3);

        t1 = new Thread(() -> {
            parkUnpark.print("a", t2);
        }, "t1");

        t2 = new Thread(() -> {
            parkUnpark.print("b", t3);
        }, "t2");

        t3 = new Thread(() -> {
            parkUnpark.print("c", t1);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();
        /* 至此，t1、t2、t3都因执行了park而进入WAITING */

        // 让t1结束WAITING，继续向下执行
        LockSupport.unpark(t1);
    }

    static class ParkUnpark {
        // 循环次数
        private int loopnum;

        public ParkUnpark() {
        }

        public ParkUnpark(int loopnum) {
            this.loopnum = loopnum;
        }

        // 按顺序打印str的核心代码
        public void print(String str, Thread next) {
            for (int i = 0; i < loopnum; i++) {
                /*
                    每个线程在执行到这时，都会被阻塞。
                    所以，需要在所有线程被阻塞之后，手动的unpark第一个被执行的线程，使得它继续向下运行。
                    然后，就会以闭环的形式输出str……
                 */

                LockSupport.park();

                System.out.print(str);

                LockSupport.unpark(next);
            }
        }
    }
}
