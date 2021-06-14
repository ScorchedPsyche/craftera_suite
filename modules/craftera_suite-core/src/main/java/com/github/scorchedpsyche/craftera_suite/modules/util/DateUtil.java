package com.github.scorchedpsyche.craftera_suite.modules.util;

import java.util.Date;

public class DateUtil {
    public static class Time
    {
        public static long getUnixNow()
        {
            return System.currentTimeMillis() / 1000L;
        }

        public static String unixToDate(long unixTime)
        {
            return new Date((long)unixTime * 1000).toString();
        }
    }
}
