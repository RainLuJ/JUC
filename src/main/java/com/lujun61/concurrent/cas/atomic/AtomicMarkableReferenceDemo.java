package com.lujun61.concurrent.cas.atomic;

import com.lujun61.concurrent.util.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author Jun Lu
 * @description 有时对于ABA问题，我们并不关心中途被修改了多少次，而只是在乎它是否被修改过
 * @date 2023-04-13 22:25:17
 */
@Slf4j
public class AtomicMarkableReferenceDemo {

    public static void main(String[] args) {
        AtomicMarkableReference<String> amr = new AtomicMarkableReference<>("A", false);

        /* step1：t1线程修改内存中的值 */
        new Thread(() -> {
            // 修改内存值，但修改前和修改后的值是【相同】的（ABA问题）
            String prev = amr.getReference();
            String next = prev;
            amr.compareAndSet(prev, next, false, true);
        }, "t1").start();

        JucUtils.sleep(1000);

        /*
            step2：main线程欲修改内存中的值失败。
            原因：内存中的值已经被t1线程修改过！
         */
        String prev = amr.getReference();
        String next = "B";
        boolean isSuccess = amr.compareAndSet(prev, next, false, true);
        log.info("CAS操作" + (isSuccess ? "成功！" : "失败！"));

        log.info(amr.getReference());
    }

}
