package com.lujun61.concurrent.cas.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;


@Slf4j
public class AtomicStampedReferenceDemo {

    // 两个参数，第一个：变量的值 第二个：版本号初始值
    public static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        String preVal = ref.getReference();
        int stamp = ref.getStamp();
        log.info("main 拿到的版本号 {}", stamp);

        other();

        TimeUnit.SECONDS.sleep(1);

        log.info("修改后的版本号 {}", ref.getStamp());
        log.info("change A->C:{}", ref.compareAndSet(preVal, "C", stamp, stamp + 1));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("{}", stamp);
            log.info("change A->B:{}", ref.compareAndSet(ref.getReference(), "B", stamp, stamp + 1));
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("{}", stamp);
            log.debug("change B->A:{}", ref.compareAndSet(ref.getReference(), "A", stamp, stamp + 1));
        }).start();
    }


}
