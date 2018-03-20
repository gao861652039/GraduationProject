package com.example.gaofeng.tulingdemo.util;

import android.util.Log;

/**
	 * 只有当isDebue() return true时，logcat 里面才能看到 error以下级别日志，如verbose,DEBUG, INFO, WARN;
	 * error始终打印在 logcat 里和 logback错误文件中.
	 * @author dongfang
	 *
	 */
public class MyLog {
    private static boolean DEBUG = false;

    /**启用debug模式*/
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**是否是Debug模式*/
    public static boolean isDebug() {
        return DEBUG;
    }

    /**
     * 输出verbose级别日志
     *
     * @param tag
     * 			      日志tag
     * @param format
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void verbose(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.v(tag, format + args.toString());
        }
    }

    /**
     * 输出debug级别日志
     * @param tag
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void debug(String tag, String args) {
        if (DEBUG) {
            Log.d(tag, args);
        }
    }

    /**
     * 输出debug级别日志
     * @param tag
     * 			日志Tag
     * @param format
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void debug(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.d(tag, format + args.toString());
        }
    }

    public static void debug(String tag, Throwable tr, String format, Object[] args) {
        if (DEBUG) {
            Log.d(tag, format + args.toString());
        }
    }

    /**
     * 输出info级别日志
     * @param tag
     * 			日志Tag
     * @param format
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void info(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.i(tag, format + args.toString());
        }
    }

    /**
     * 输出error级别日志
     * @param tag
     * 			日志Tag
     * @param format
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void error(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.e(tag, format + args.toString());
        }
    }

    /**
     * 输出warn级别日志
     * @param tag
     * 			日志Tag
     * @param format
     *            日志格式
     * @param args
     *            替换参数
     */
    public static void warn(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.w(tag, format + args.toString());
        }
    }

}
