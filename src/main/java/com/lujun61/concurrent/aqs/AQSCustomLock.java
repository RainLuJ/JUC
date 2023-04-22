package com.lujun61.concurrent.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @description 基于AQS实现自定义锁：独占、不可重入锁
 * @author Jun Lu
 * @date 2023-04-22 09:43:03
 */
public class AQSCustomLock implements Lock {

    /**
     * @description 加锁状态枚举类
     * @author Jun Lu
     * @date 2023-04-22 09:51:32
     */
    enum LockState {
        NORMAL("无锁状态", 0),
        LOCK("被加锁状态", 1)
        ;

        private final String lockState;
        private final int state;

        LockState(String lockState, int state) {
            this.lockState = lockState;
            this.state = state;
        }

        public String getLockState() {
            return lockState;
        }

        public int getState() {
            return state;
        }

        @Override
        public String toString() {
            return "LockState{" +
                    "lockState='" + lockState + '\'' +
                    ", state=" + state +
                    '}';
        }
    }

    /**
     * @description 自定义同步器类：目的是实现独占锁
     * @author Jun Lu
     * @date 2023-04-22 09:42:52
     */
    class MyAnsynchronizer extends AbstractQueuedSynchronizer {

        @Override
        // 尝试获取锁
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(LockState.NORMAL.getState(), LockState.LOCK.getState())) {
                // 若加上了锁，则设置“独占锁所有者”为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        // 释放锁
        protected boolean tryRelease(int arg) {
            try {
                // 解锁逻辑：设置“独占锁所有者”为null
                setExclusiveOwnerThread(null);
                // 由于state变量是volatile的，则能保证有序性。
                // 所以，应该写在setExclusiveOwnerThread()方法之后，充分发挥写屏障的作用
                setState(LockState.NORMAL.getState());
                return true;
            } catch (Exception e) {   // 出现异常，解锁失败
                e.printStackTrace();
                return false;
            }
        }

        @Override
        // 当前线程是否持有锁
        protected boolean isHeldExclusively() {
            return getState() == LockState.LOCK.getState();
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final MyAnsynchronizer sync = new MyAnsynchronizer();

    @Override
    // 加锁不成功会进入阻塞队列，直到获取到锁为止
    public void lock() {
        // 当前的加锁逻辑是不能使用MyAnsynchronizer中重写的tryAcquire方法的
        // 因为tryAcquire是tryLock()方法的实现逻辑！
        sync.acquire(LockState.LOCK.getState());
    }

    @Override
    // 加锁，可被打断。若加锁不成功会进入阻塞队列，直到获取到锁为止
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(LockState.LOCK.getState());
    }

    @Override
    // 尝试加锁。加锁成功则返回true；加锁失败则返回false，而不会进入阻塞队列。
    public boolean tryLock() {
        try {
            sync.tryAcquire(LockState.LOCK.getState());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    // 尝试在给定的超时时间内加锁。加锁成功则返回true；加锁失败则返回false，而不会进入阻塞队列。
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(LockState.LOCK.getState(), unit.toNanos(time));
    }

    @Override
    // 解锁
    public void unlock() {
        sync.release(LockState.NORMAL.getState());
    }

    @Override
    // 新建一个条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}
