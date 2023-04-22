package com.lujun61.concurrent.cas.atomic;

import com.lujun61.concurrent.util.JucUtils;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterDemo {

    public static AtomicReferenceFieldUpdater<Student, String> ref =
            AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

    public static void main(String[] args) throws InterruptedException {
        Student student = new Student();

        new Thread(() -> {
            System.out.println(ref.compareAndSet(student, null, "list"));
        }, "t1").start();

        // 确保t1线程先执行
        JucUtils.sleep(100);

        System.out.println(ref.compareAndSet(student, null, "张三"));
        System.out.println(student);  // Student{name='list'}
    }

    static class Student {

        public volatile String name;

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
