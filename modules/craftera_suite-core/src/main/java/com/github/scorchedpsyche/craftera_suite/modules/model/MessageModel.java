package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.util.MessageUtil;
import org.bukkit.ChatColor;

public class MessageModel
{
    public MessageModel()
    {
        stringBuilder = new StringBuilder();
    }

    private StringBuilder stringBuilder;

    public MessageModel aqua(String str)
    {
        stringBuilder.append(ChatColor.AQUA);
        addMessageAndReset(str);

        return this;
    }

    public MessageModel red(String str)
    {
        stringBuilder.append(ChatColor.RED);
        addMessageAndReset(str);

        return this;
    }

    private void addMessageAndReset(String str)
    {
        stringBuilder.append(str);
        stringBuilder.append(ChatColor.RESET);
    }

    public String toString()
    {
        return stringBuilder.toString();
    }
}
