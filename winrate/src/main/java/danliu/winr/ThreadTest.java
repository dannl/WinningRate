package danliu.winr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by danliu on 16-8-11.
 */
public class ThreadTest {

    public static void main(String[] args) {
        testThreadName();
    }

    private static void testThreadName() {
        System.out.println(Thread.currentThread().getName());
        Thread.currentThread().setName("mainThread");
        System.out.println(Thread.currentThread().getName());
        final Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                Thread.currentThread().setName("subThread");
                System.out.println(Thread.currentThread().getName());
            }
        };
        t.start();
    }

    private static void testThread() {
        String[] names = new String[] {"A","B","C","D","E","F"};
        SHolder holder = new SHolder();
        List<TestThread> all = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TestThread testThread = new TestThread(names[i], holder);
            testThread.start();
            all.add(testThread);
        }
        for (int i = 0; i < all.size(); i++) {
            try {
                all.get(i).join();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("main process end!");
    }

    private static class TestThread extends Thread {

        SHolder mSHolder;
        String mName;

        TestThread(String name, SHolder holder) {
            mSHolder = holder;
            mName = name;
        }

        @Override
        public void run() {
            System.out.println(mName + "_started!");
            Object o = mSHolder.next();
            System.out.println(mName + "_got object!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            System.out.println(mName + "_end!");
            mSHolder.recycle(o);
            System.out.println(mName + "_recycle called!");
        }
    }

    private static class SHolder {

        private final Stack<Object> mStack;

        SHolder() {
            mStack = new Stack<>();
            for (int i = 0; i < 3; i++) {
                mStack.add(new Object());
            }
        }

        public Object next() {
            synchronized (mStack) {
                while (mStack.isEmpty()) {
                    try {
                        mStack.wait();
                    } catch (InterruptedException e) {

                    }
                }
            }
            return mStack.pop();
        }

        public void recycle(Object o) {
            synchronized (mStack) {
                mStack.push(o);
                mStack.notify();
            }
        }

    }
}
