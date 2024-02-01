package com.bil.spring.web;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * TODO 又忘记写注释了？
 *
 * @author haibo.yang
 * @since 2022/2/7
 */
public class Counter {

    public static int count = 0;

    public synchronized void inc() {
        System.out.println(count++);
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(20);
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    c1.inc();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    c2.inc();
                }
            }).start();
        }
    }
}
