package com.github.scorchedpsyche.craftera_suite.modules.model;

import org.bukkit.ChatColor;

public class StringFormattedModel
{
    public StringFormattedModel()
    {
        stringBuilder = new StringBuilder();
    }

    private StringBuilder stringBuilder;

    public StringFormattedModel aqua(String str)
    {
        stringBuilder.append(ChatColor.AQUA);
        add(str);

        return this;
    }
    public StringFormattedModel aquaR(String str)
    {
        stringBuilder.append(ChatColor.AQUA);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel darkGreen(String str)
    {
        stringBuilder.append(ChatColor.DARK_GREEN);
        add(str);

        return this;
    }
    public StringFormattedModel darkGreenR(String str)
    {
        stringBuilder.append(ChatColor.DARK_GREEN);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel gold(String str)
    {
        stringBuilder.append(ChatColor.GOLD);
        add(str);

        return this;
    }
    public StringFormattedModel goldR(String str)
    {
        stringBuilder.append(ChatColor.GOLD);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel gray(String str)
    {
        stringBuilder.append(ChatColor.GRAY);
        add(str);

        return this;
    }
    public StringFormattedModel grayR(String str)
    {
        stringBuilder.append(ChatColor.GRAY);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel green(String str)
    {
        stringBuilder.append(ChatColor.GREEN);
        add(str);

        return this;
    }
    public StringFormattedModel greenR(String str)
    {
        stringBuilder.append(ChatColor.GREEN);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel red(String str)
    {
        stringBuilder.append(ChatColor.RED);
        add(str);

        return this;
    }
    public StringFormattedModel redR(String str)
    {
        stringBuilder.append(ChatColor.RED);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel white(String str)
    {
        stringBuilder.append(ChatColor.WHITE);
        add(str);

        return this;
    }
    public StringFormattedModel whiteR(String str)
    {
        stringBuilder.append(ChatColor.WHITE);
        addAndReset(str);

        return this;
    }

    public StringFormattedModel yellow(String str)
    {
        stringBuilder.append(ChatColor.YELLOW);
        add(str);

        return this;
    }
    public StringFormattedModel yellowR(String str)
    {
        stringBuilder.append(ChatColor.YELLOW);
        addAndReset(str);

        return this;
    }

    private StringFormattedModel addAndReset(String str)
    {
        add(str);
        stringBuilder.append(ChatColor.RESET);

        return this;
    }

    public StringFormattedModel add(String str)
    {
        stringBuilder.append(str);

        return this;
    }

    public StringFormattedModel reset()
    {
        stringBuilder.append(ChatColor.RESET);

        return this;
    }

    public StringFormattedModel nl()
    {
        stringBuilder.append("\n");

        return this;
    }

    public StringFormattedModel bold()
    {
        stringBuilder.append(ChatColor.BOLD);

        return this;
    }

    public String toString()
    {
        return stringBuilder.toString();
    }
}