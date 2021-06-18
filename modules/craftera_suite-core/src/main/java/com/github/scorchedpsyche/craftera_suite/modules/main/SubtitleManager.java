package com.github.scorchedpsyche.craftera_suite.modules.main;

import java.util.LinkedList;

public class SubtitleManager {
    private StringBuilder subtitleMessage = new StringBuilder();

    public boolean isEmpty()
    {
        return subtitleMessage.length() == 0;
    }

    public String getText()
    {
        return subtitleMessage.toString();
    }

    public void reset()
    {
        subtitleMessage.setLength(0);
    }

    public void addToStart(String text)
    {
        subtitleMessage.insert(0, text);
    }

    public void addToStart(StringBuilder text)
    {
        subtitleMessage.insert(0, text);
    }

    public void addToEnd(String text)
    {
        subtitleMessage.append(text);
    }

    public void addToEnd(StringBuilder text)
    {
        subtitleMessage.append(text);
    }
}
