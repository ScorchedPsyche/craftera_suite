package com.github.scorchedpsyche.craftera_suite.modules.utils.natives;

public class StringUtils
{
    public boolean isNullOrEmpty(String string)
    {
        return string == null || string.isEmpty();
    }
    public boolean isStringBuilderNullOrEmpty(StringBuilder stringBuilder)
    {
        return stringBuilder == null || stringBuilder.equals("");
    }
}
