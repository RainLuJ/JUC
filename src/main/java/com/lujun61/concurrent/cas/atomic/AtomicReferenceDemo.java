package com.lujun61.concurrent.cas.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description 原子引用
 * @author Jun Lu
 * @date 2023-04-14 22:24:55
 */
@Slf4j
public class AtomicReferenceDemo {

    public static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        String preVal = ref.get();

        // 等待其它两个线程执行ABA操作
        other();

        TimeUnit.SECONDS.sleep(1);
        // 更新成功！因为AtomicReference中的CAS操作无法感知到ABA问题
        log.debug("change A->C {}", ref.compareAndSet(preVal, "C"));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.get(), "B"));
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.get(), "A"));
        }, "t2").start();
    }

}
