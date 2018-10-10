package smpp.utils;

import java.util.Date;

/**
 * Created by luyenchu on 6/30/16.
 */
public class Test implements Runnable {
    public static void main(String[] args) {
        Test t = new Test();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        t1.start();
        t2.start();
    }

    public void print(String sms) {
        System.out.println(sms);
    }

    @Override
    public void run() {
        print("<1>Xin chao!!!:  " + new Date());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        print("<2>Xin chao!!!:  " + new Date());
    }
}
