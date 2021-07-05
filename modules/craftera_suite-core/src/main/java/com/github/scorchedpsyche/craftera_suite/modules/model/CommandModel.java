package com.github.scorchedpsyche.craftera_suite.modules.model;

import java.util.HashMap;

public class CommandModel
{
    public CommandModel()
    {
    }

    public CommandModel(String permission)
    {
        this.permission = permission;
    }

    public CommandModel(boolean playersAsSubcommands)
    {
        this.playersAsSubcommands = playersAsSubcommands;
    }

    public CommandModel(String permission, boolean playersAsSubcommands)
    {
        this.permission = permission;
        this.playersAsSubcommands = playersAsSubcommands;
    }

    public HashMap<String, CommandModel> subCommands;
    private String lastKey;
    private String permission;
    private boolean playersAsSubcommands = false;

    public CommandModel addCommand(String commandName)
    {
        if ( subCommands == null )
        {
            subCommands = new HashMap<>();
        }

        subCommands.put(commandName, new CommandModel());
        lastKey = commandName;

        return this;
    }

    public CommandModel addSubcommand(String commandName)
    {
        if ( subCommands.get(lastKey).subCommands == null )
        {
            subCommands.get(lastKey).subCommands = new HashMap<>();
        }

        subCommands.get(lastKey).subCommands.put(commandName, null);

        return this;
    }

    public CommandModel addSubcommands(HashMap<String, CommandModel> commands)
    {
        if ( subCommands == null )
        {
            subCommands = new HashMap<>();
        }

        subCommands.putAll(commands);

        return this;
    }

    public boolean hasSubcommands()
    {
        if( playersAsSubcommands )
        {
            return true;
        }

        return subCommands != null;
    }
    public String getPermission(){ return permission; }

    public boolean arePlayersSubcommands() {
        return playersAsSubcommands;
    }
}
