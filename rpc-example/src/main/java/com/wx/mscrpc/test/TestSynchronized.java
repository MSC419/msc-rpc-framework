package com.wx.mscrpc.test;

/**
 * @Description 测试代码
 * @Author MSC419
 * @Date 2022/4/3 16:26
 * @Version 1.0
 */
public class TestSynchronized {
    public static void main(String[] args) {
//        System.out.println("使用关键字synchronized");
        System.out.println("不使用关键字synchronized");

        SyncThread syncThread = new SyncThread();
        Thread thread1 = new Thread(syncThread, "SyncThread1");
        Thread thread2 = new Thread(syncThread, "SyncThread2");
        thread1.start();
        thread2.start();
    }
}

class SyncThread implements Runnable {
    private static int count;
    public SyncThread() {
        count = 0;
    }
    public  void run() {
//        synchronized (this){
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println("线程名:"+Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//    }
    public int getCount() {
        return count;
    }
}
