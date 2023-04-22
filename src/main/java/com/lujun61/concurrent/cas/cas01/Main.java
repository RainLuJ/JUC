package com.lujun61.concurrent.cas.cas01;

public class Main {

    public static void main(String[] args) {
        test01();

        System.out.println("================ 分割线 ================");

        test02();
    }

    public static void test01() {
        System.out.println("synchronized：");
        Account account = new AccountSynchronizedImpl(10000);
        Account.demo(account);
    }

    public static void test02() {
        System.out.println("CAS：");
        Account account = new AccountCASImpl(10000);
        Account.demo(account);
    }

}
