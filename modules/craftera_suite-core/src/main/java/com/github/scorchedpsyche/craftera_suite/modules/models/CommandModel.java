package com.github.scorchedpsyche.craftera_suite.modules.models;

import org.bukkit.event.Event;

import java.util.HashMap;

public class CommandModel
{
    public CommandModel()
    {
    }

    public CommandModel(Event eventToCall)
    {
        this.eventToCall = eventToCall;
    }

    public CommandModel(Event eventToCall, HashMap<String, CommandModel> subCommands)
    {
        this.eventToCall = eventToCall;
        this.subCommands = subCommands;
    }

    public Event eventToCall;
    public HashMap<String, CommandModel> subCommands;
}
