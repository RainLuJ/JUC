package com.lujun61.concurrent.cas.cas01;

/**
 * @description 使用Synchronized实现存取款操作
 * @author Jun Lu
 * @date 2023-04-14 15:50:05
 */
public class AccountSynchronizedImpl implements Account {

    private Integer balance;

    public AccountSynchronizedImpl(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) { // 加锁。
            this.balance -= amount;
        }
    }
}
