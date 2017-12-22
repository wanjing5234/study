package com.kk.study.framework.sigar;

public class RuntimeTest {

    public static void main(String[] args) {
        try {

            // System信息，从jvm获取
            SigarUtil.property();
            System.out.println("----------------------------------");

            // cpu信息
            SigarUtil.cpu();
            System.out.println("----------------------------------");

            // 内存信息
            SigarUtil.memory();
            System.out.println("----------------------------------");

            // 操作系统信息
            SigarUtil.os();
            System.out.println("----------------------------------");

            // 用户信息
            SigarUtil.who();
            System.out.println("----------------------------------");

            // 文件系统信息
            SigarUtil.file();
            System.out.println("----------------------------------");

            // 网络信息
            SigarUtil.net();
            System.out.println("----------------------------------");

            // 以太网信息
            SigarUtil.ethernet();
            System.out.println("----------------------------------");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


}