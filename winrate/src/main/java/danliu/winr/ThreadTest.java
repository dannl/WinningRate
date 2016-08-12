package danliu.winr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danliu on 16-8-11.
 */
public class ThreadTest {

    private static final Pattern CITY_PATTERN = Pattern.compile("<City ID=\"(\\d+)\" CityName=\"([\u4E00-\u9FA5]+)\" PID=\"(\\d+)\" ZipCode=\"(\\d+)\"");
    private static final Pattern PROVINCE_PATTERN = Pattern.compile("<Province ID=\"\\d+\" ProvinceName=\"([\u4E00-\u9FA5]+)\"");
    private static final Pattern DISTRICT_PATTERN = Pattern.compile("<District ID=\"\\d+\" DistrictName=\"([\u4E00-\u9FA5]+)\" CID=\"(\\d+)\"");


    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5]+$");

        try {
            String city = SimpleIOUtils.loadContent(new FileInputStream(new File(SimpleIOUtils.getProjectRoot(), "winrate/src/main/resources/Cities.xml")));
            String province = SimpleIOUtils.loadContent(new FileInputStream(new File(SimpleIOUtils.getProjectRoot(), "winrate/src/main/resources/Provinces.xml")));
            String district = SimpleIOUtils.loadContent(new FileInputStream(new File(SimpleIOUtils.getProjectRoot(), "winrate/src/main/resources/Districts.xml")));

            StringBuilder builder = new StringBuilder();

            Matcher matcher = CITY_PATTERN.matcher(city);
            while(matcher.find()) {
                builder.append(
                        String.format("INSERT INTO city (name,pid,zip_code) VALUES ('%s',%s,%s)", matcher.group(2), matcher.group(3), matcher.group(4)))
                .append(";\n");
            }

            matcher = PROVINCE_PATTERN.matcher(province);
            while (matcher.find()) {
                builder.append(
                        String.format("INSERT INTO province (name) VALUES ('%s')", matcher.group(1))
                ).append(";\n");
            }

            matcher = DISTRICT_PATTERN.matcher(district);
            while (matcher.find()) {
                builder.append(String.format("INSERT INTO district (name,cid) VALUES ('%s',%s)", matcher.group(1), matcher.group(2)))
                .append(";\n");
            }
            System.out.println(builder.toString());
            SimpleIOUtils.saveToFile(new File(SimpleIOUtils.getProjectRoot(), "winrate/src/main/resources/result"), builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
