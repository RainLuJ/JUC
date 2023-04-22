package com.lujun61.concurrent.threadpool.custom;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description 自定义阻塞队列，存放来不及被线程池中线程消费的任务。
 *              平衡消费者线程与生产者线程速率的一个桥梁。
 * @author Jun Lu
 * @date 2023-04-18 13:44:16
 */
@Slf4j
public class BlockingQueue<T> {

    // 1、任务队列：从队列首部取元素(removeFirst)，添加元素至队列尾部()
    private Deque<T> queue = new ArrayDeque<>();

    // 2、锁
    private ReentrantLock lock = new ReentrantLock();

    // 3、生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 4、消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5、阻塞队列容量
    private int capacity;

    public BlockingQueue() {
    }

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @param rejectPolicy 自定义拒绝策略
     * @param task 待处理任务，分为两类
     *             - 可以正常加入队列的任务
     *             - 无法加入队列的任务
     * @description 支持拒绝策略的入队操作
     * @author Jun Lu
     * @date 2023-04-18 19:14:29
     */
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();

        try {
            // 如果队列已满，则需要使用调用者自定义的拒绝策略来处理溢出任务
            if (isFull()) {
                log.debug("任务【{}】使用自定义拒绝策略", task.hashCode());
                rejectPolicy.reject(this, task);
            } else {  // 队列未满时，正常将任务加入队列
                log.debug("任务【{}】加入队列", task.hashCode());
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    // [超时]阻塞获取：注意因【虚假唤醒】时，而导致的不正确等待时长
    public T take(long timeout, TimeUnit timeUnit) {
        lock.lock();

        try {   // 外层try...finally只负责释放锁，而不负责处理异常
            long nanos = timeUnit.toNanos(timeout);
            // 如果队列为空，则无法继续获取元素，需要陷入阻塞
            while (isEmpty()) {
                try {
                    // 超过最长等待时间时，不必继续等待
                    if (nanos <= 0) {
                        log.info("元素获取超时！");
                        return null;
                    }

                    // awaitNanos返回值就是经过一番等待之后，当前还应该再等待多久
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 当队列不为空，线程将获得队列首元素
            T headEle = queue.removeFirst();
            // 队列中元素被消耗了一个，则唤醒由于【队列满队而陷入等待】的线程
            fullWaitSet.signal();
            return headEle;
        } finally {
            lock.unlock();
        }
    }

    // [超时]阻塞添加
    public boolean put(T ele, long timeout, TimeUnit timeUnit) {
        lock.lock();

        try {   // 外层try...finally只负责释放锁，而不负责处理异常
            long nanos = timeUnit.toNanos(timeout);
            // 如果队列中的元素个数与队列的最大容量相等，则无法继续添加元素，需要陷入阻塞
            while (isFull()) {
                try {
                    // 超过最长等待时间时，不必继续等待
                    if (nanos <= 0) {
                        log.info("添加元素超时！");
                        return false;
                    }

                    // awaitNanos返回值就是经过一番等待之后，当前还应该再等待多久
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.addLast(ele);
            // 队列中有元素了，则唤醒由于【队列为空而陷入等待】的线程消费
            emptyWaitSet.signal();

            return true;
        } finally {
            lock.unlock();
        }
    }


    // [无限期]阻塞获取
    public T take() {
        lock.lock();

        try {   // 外层try...finally只负责释放锁，而不负责处理异常
            // 如果队列为空，则无法继续获取元素，需要陷入阻塞
            while (isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 当队列不为空，线程将获得队列首元素
            T headEle = queue.removeFirst();
            // 队列中元素被消耗了一个，则唤醒由于【队列满队而陷入等待】的线程
            fullWaitSet.signal();
            return headEle;
        } finally {
            lock.unlock();
        }
    }

    // [无限期]阻塞添加
    public void put(T ele) {
        lock.lock();

        try {   // 外层try...finally只负责释放锁，而不负责处理异常
            // 如果队列中的元素个数与队列的最大容量相等，则无法继续添加元素，需要陷入阻塞
            while (isFull()) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.addLast(ele);
            // 队列中有元素了，则唤醒由于【队列为空而陷入等待】的线程消费
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 获取队列中，【当前】元素个数。在高并发场景下，也是需要用锁来控制的
    public int size() {
        lock.lock();

        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    // 判断当前队列是否为空
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // 判断当前队列是否已满
    public boolean isFull() {
        return this.size() == capacity;
    }

}

















