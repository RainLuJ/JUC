package com.lujun61.concurrent.active.deadlock;

public class DeadLockDemo {

    public static void main(String[] args) {
        final Object A = new Object();
        final Object B = new Object();
        new Thread(()->{
            synchronized (A) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {

                }
            }
        }).start();

        new Thread(()->{
            synchronized (B) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {

                }
            }
        }).start();
    }


}
