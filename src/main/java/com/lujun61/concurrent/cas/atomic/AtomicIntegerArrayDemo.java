package com.lujun61.concurrent.cas.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class AtomicIntegerArrayDemo {
    // https://blog.csdn.net/ch123456hy/article/details/109499727
    private static volatile Integer[] arry = new Integer[10];
    static {
        Arrays.fill(arry, 0);
    }

    public static void main(String[] args) throws InterruptedException {
        //demo(
        //        () -> new int[10],
        //        (array) -> array.length,
        //        (array, index) -> array[index]++,
        //        (array) -> System.out.println(Arrays.toString(array))
        //);

        demo(
                () -> arry,
                (array) -> array.length,
                (array, index) -> array[index]++,
                (array) -> System.out.println(Arrays.toString(array))
        );

        TimeUnit.SECONDS.sleep(1);

        demo(
                () -> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                (array) -> System.out.println(array)
        );
    }

    private static <T> void demo(
            /* 提供者：无中生有一个对象 */
            Supplier<T> arraySupplier,
            /* 函数：一个参数一个结果【(参数1)->结果】；BiFunction：两个参数，一个结果【(参数1, 参数2)->结果】 */
            Function<T, Integer> lengthFun,
            /* 消费者：两个参数没结果【(参数1, 参数2)->void】 */
            BiConsumer<T, Integer> putConsumer,
            /* 消费者：一个参数没结果【(参数1)->void】 */
            Consumer<T> printConsumer) {

        ArrayList<Thread> ts = new ArrayList<>(); // 创建集合
        T array = arraySupplier.get(); // 获取数组
        int length = lengthFun.apply(array); // 获取数组的长度

        for (int i = 0; i < length; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % length);
                }
            }));
        }

        ts.forEach(Thread::start);  // 启动所有线程
        ts.forEach((thread) -> {    // 等待所有线程执行完毕
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        printConsumer.accept(array);
    }


}
