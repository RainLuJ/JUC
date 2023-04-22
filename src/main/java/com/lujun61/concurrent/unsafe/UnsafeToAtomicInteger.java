package com.lujun61.concurrent.unsafe;

import com.lujun61.concurrent.cas.cas01.Account;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description 使用 Unsafe 模拟实现原子整数
 * @author Jun Lu
 * @date 2023-04-18 10:16:45
 */
public class UnsafeToAtomicInteger {

    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }

}

class MyAtomicInteger implements Account {

    private volatile Integer value;
    private static final Unsafe UNSAFE;
    private static final long valueOffset;

    static {

        try {
            /* 反射创建 unsafe 对象 */
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            // 获取Field属性值：由于Unsafe中的theUnsafe属性为静态的，则不需要使用对象便可以获取，即传入null值即可
            UNSAFE = (Unsafe) theUnsafe.get(null);

            /* 获取自定义原子整数类中的value属性的偏移量 */
            valueOffset = UNSAFE.objectFieldOffset
                    (MyAtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public MyAtomicInteger(Integer value) {
        this.value = value;
    }

    public Integer get() {
        return value;
    }

    public void decrement(Integer amount) {
        Integer preVal;
        Integer nextVal;
        do {
            preVal = this.value;
            nextVal = preVal - amount;
        } while (!UNSAFE.compareAndSwapObject(this, valueOffset, preVal, nextVal));
    }

    @Override
    public Integer getBalance() {
        return get();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}
