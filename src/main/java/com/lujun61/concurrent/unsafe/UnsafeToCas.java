package com.lujun61.concurrent.unsafe;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description Unsafe 模拟实现 CAS 操作
 * @author Jun Lu
 * @date 2023-04-18 10:24:25
 */
public class UnsafeToCas {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        /* 创建 unsafe 对象 */
        // 获取Unsafe类中提供的Unsafe对象
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        // 获取Field属性值：由于Unsafe中的theUnsafe属性为静态的，则不需要使用对象便可以获取，即传入null值即可
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        // 拿到偏移量
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        // 进行 cas 操作
        Teacher teacher = new Teacher();
        unsafe.compareAndSwapLong(teacher, idOffset, 0, 100);
        unsafe.compareAndSwapObject(teacher, nameOffset, null, "lisi");

        System.out.println(teacher);
    }

}

@Data
class Teacher {

    private volatile int id;
    private volatile String name;

}