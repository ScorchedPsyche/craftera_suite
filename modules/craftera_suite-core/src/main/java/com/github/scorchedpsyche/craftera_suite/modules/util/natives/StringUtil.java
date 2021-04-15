package com.github.scorchedpsyche.craftera_suite.modules.util.natives;

public class StringUtil
{
    public static boolean isNullOrEmpty(String string)
    {
        return string == null || string.isEmpty();
    }
    public static boolean isStringBuilderNullOrEmpty(StringBuilder stringBuilder)
    {
        return stringBuilder == null || stringBuilder.equals("");
    }
}
