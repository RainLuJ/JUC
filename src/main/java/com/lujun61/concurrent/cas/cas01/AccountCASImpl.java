package com.lujun61.concurrent.cas.cas01;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description 使用CAS实现存取款操作
 * @author Jun Lu
 * @date 2023-04-14 15:50:05
 */
public class AccountCASImpl implements Account {

    AtomicInteger atomicInteger;

    public AccountCASImpl(Integer balance) {
        this.atomicInteger = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return atomicInteger.get();
    }

    @Override
    public void withdraw(Integer amount) {
        // 核心代码
        int pre, next;
        do {
            pre = getBalance();
            next = pre - amount;
        } while (!atomicInteger.compareAndSet(pre, next));
    }
}
