package com.lujun61.concurrent.dsgnpattern.pattern03.alternately;

public class AlternatelyUseWaitNotify {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 3);

        Thread t1 = new Thread(() -> {
            waitNotify.print("a", 1, 2);
        }, "t1");

        Thread t2 = new Thread(() -> {
            waitNotify.print("b", 2, 3);
        }, "t2");

        Thread t3 = new Thread(() -> {
            waitNotify.print("c", 3, 1);
        }, "t3");

        t1.start();
        //JucUtils.sleep(500);
        t2.start();
        t3.start();
    }

    static class WaitNotify {
        /*
            标记当前可被执行的线程：
                · 1：t1可被执行
                · 2：t2可被执行
                · 3：t3可被执行
         */
        private int flag;
        private int loopnum;

        public WaitNotify() {
        }

        public WaitNotify(int flag, int loopnum) {
            this.flag = flag;
            this.loopnum = loopnum;
        }

        /**
         * @param currFlag 当前的执行标记
         * @param nextFlag 下一个执行标记
         * @description 按顺序打印abcabcabc
         * @author Jun Lu
         * @date 2023-04-11 16:33:25
         */
        public void print(String str, int currFlag, int nextFlag) {
            for (int i = 0; i < loopnum; i++) {
                synchronized (this) {
                    while (flag != currFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(str);
                    this.flag = nextFlag;
                    this.notifyAll();
                }
            }
        }
    }

}
