package com.github.scorchedpsyche.craftera_suite.modules.util;

public class MathUtil
{
    int upperround(int num, int base) {
        int temp = num%base;
        return (temp == 0 ? num : num + base - temp);
    }
    public static int roundToMultiple(int num, int base) {
        return num - num % base + (num%base==0? 0 : base);
    }
    public static int limitBetween(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
