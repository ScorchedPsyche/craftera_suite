package com.github.scorchedpsyche.craftera_suite.modules.util;

public class DateUtil {
    public static class Time
    {
        public static long getUnixNow()
        {
            return System.currentTimeMillis() / 1000L;
        }
    }
}
