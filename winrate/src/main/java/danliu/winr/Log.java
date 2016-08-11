package danliu.winr;

/**
 * Log for threads.
 * Created by danliu on 16-8-11.
 */
public class Log {

    private static final String MSG_FORMAT = "[%s]%s";

    private Log() {}

    public static void i(String ...msg) {
        if (msg == null || msg.length == 0) {
            return;
        }
        i(join("-", msg));
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(Thread thread, String... msg) {
        if (msg == null || msg.length == 0) {
            return;
        }
        i(thread, join("-", msg));
    }

    public static void i(Thread thread, String msg) {
        String threadName;
        if (thread == null) {
            threadName = "main";
        } else {
            threadName = thread.getName();
        }
        System.out.println(String.format(MSG_FORMAT, threadName, msg));
        //TODO update status.

    }

    private static String join(String separator, String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

}
