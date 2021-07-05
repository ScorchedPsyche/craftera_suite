package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;

import java.util.HashMap;
import java.util.Stack;

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

    public HashMap<String, CommandModel> subCommands = new HashMap<>();
    Stack<String> breadcrumbs = new Stack<>();
    @Deprecated
    private String lastKey;
    private String permission;
    private boolean playersAsSubcommands = false;

    /**
     * Adds a sibling command
     * @param commandName Name of the command
     * @return Fluent API builder
     */
    public CommandModel sibling(String commandName)
    {
        if( !breadcrumbs.empty() )
        {
            siblingRecursive(commandName, subCommands.get(breadcrumbs.firstElement()), 1);
        } else {
            subCommands.put(commandName, new CommandModel());
            breadcrumbs.push(commandName);
        }

        return this;
    }

    /**
     * Recursive method that adds the command at the proper location
     * @param commandName Name of the command
     */
    private void siblingRecursive(String commandName, CommandModel childCommands, int index)
    {
        if( index == breadcrumbs.size() - 1 )
        {
            childCommands.subCommands.put(commandName, new CommandModel());
            if(breadcrumbs.size() > 0)
            {
                breadcrumbs.pop();
            }
            breadcrumbs.push(commandName);
        } else {
            siblingRecursive(commandName, childCommands.subCommands.get(breadcrumbs.get(index)), ++index);
        }
    }

    /**
     * Adds a child command
     * @param commandName Name of the command
     * @return Fluent API builder
     */
    public CommandModel child(String commandName)
    {
        childRecursive(commandName, subCommands.get(breadcrumbs.firstElement()), 0);

        return this;
    }

    /**
     * Recursive method that adds the command at the proper location
     * @param commandName Name of the command
     */
    private void childRecursive(String commandName, CommandModel parentCommands, int index)
    {
        if( index == breadcrumbs.size() - 1 )
        {
            parentCommands.subCommands.put(commandName, new CommandModel());
            breadcrumbs.push(commandName);
        } else {
            childRecursive(commandName, parentCommands.subCommands.get(breadcrumbs.get(++index)), index);
        }
    }

    /**
     * Returns to the parent from the current depth
     * @return Fluent API builder
     */
    public CommandModel back()
    {
        breadcrumbs.pop();

        return this;
    }

    @Deprecated
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

    @Deprecated
    public CommandModel addSubcommand(String commandName)
    {
        if ( subCommands.get(lastKey).subCommands == null )
        {
            subCommands.get(lastKey).subCommands = new HashMap<>();
        }

        subCommands.get(lastKey).subCommands.put(commandName, null);

        return this;
    }

    @Deprecated
    public CommandModel addSubcommands(HashMap<String, CommandModel> commands)
    {
        if ( subCommands == null )
        {
            subCommands = new HashMap<>();
        }

        subCommands.putAll(commands);

        return this;
    }

    @Deprecated
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
