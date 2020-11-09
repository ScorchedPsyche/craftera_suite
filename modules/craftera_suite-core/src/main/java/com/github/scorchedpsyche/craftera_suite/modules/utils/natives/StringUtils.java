package com.github.scorchedpsyche.craftera_suite.modules.utils.natives;

public class StringUtils
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
