package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CustomTabCompleter implements TabCompleter {
    public static HashMap<String, CommandModel> commands;

    public CustomTabCompleter() {
        super();

        commands = new HashMap<>();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Check if sender is player
        if (sender instanceof Player) {
            // Check for CES command
            if ( command.getName().equalsIgnoreCase("ces") ) {
                // Root CES command. Return options

                // Check if more than 1 argument
                if( args.length > 0  )
                {
                    // At root command: /ces
                    List<String> arguments = new ArrayList<>(Arrays.asList(args));

                    User user = CraftEraSuiteCore.luckPerms.getUserManager().getUser(((Player) sender).getUniqueId());

                    if( user != null )
                    {
                        return getSubcommands( user, commands, arguments);
                    } else {
                        ConsoleUtil.logError(SuitePluginManager.Core.Name.full,
                                "Failed to get user's permission from Luck Perms.");
                        PlayerUtil.sendMessageWithPluginPrefix(((Player) sender).getPlayer(),
                                SuitePluginManager.Core.Name.compact,
                                "Failed to get user's permission from Luck Perms. Let your server admin know!!!");
                    }
                }
            }
        }

        return null;
    }

    private List<String> getSubcommands(User user, HashMap<String, CommandModel> commands, List<String> args)
    {
        // Check if there's at least a valid string for the arguments
        if( args.size() > 0 && !StringUtil.isNullOrEmpty(args.get(0)) )
        {
            // At least one valid argument

            // Check if there's exactly one argument
            if( args.size() == 1 )
            {
                // 1 arg. Return partial filtered list by word
                return filterListByPartialWordAndPermission(user, commands, args.get(0));
            } else {
                // More than 1 arg. Check if current command tree contains the arg with subcommands
                if(     commands.containsKey(args.get(0)) &&
                        commands.get(args.get(0)) != null &&
                        commands.get(args.get(0)).hasSubcommands() &&
                        CraftEraSuiteCore.userHasPermission( user, commands.get(args.get(0)).getPermission() ) )
                {
                    // Has subcommands
                    HashMap<String, CommandModel> subCommands = commands.get(args.get(0)).subCommands;

                    // Remove parent argument and check child subcommands
                    args.remove(0);
                    return getSubcommands(user, subCommands, args);
                }

                // No subcommands. Suggest nothing
                return new ArrayList<>();
            }
        }

        // No valid string for argument. Return current tree position
        List<String> filteredList = new ArrayList<>();

        // Loop through commands
        for (Map.Entry<String, CommandModel> currentCommand : commands.entrySet())
        {
            // Check if user has permission to use the command
            if( currentCommand.getValue() == null || CraftEraSuiteCore.userHasPermission(user, currentCommand.getValue().getPermission()) )
            {
                // Has permission. Add to filtered list
                filteredList.add(currentCommand.getKey());
            }
        }

        return filteredList;
    }

    /**
     * Returns a list filtered with words that start with the given string.
     * @param commandsToBeFiltered The list to search the string in
     * @param searchString The word to search for
     * @return Returns an empty list if no items that start with the provided word were found.
     */
    private List<String> filterListByPartialWordAndPermission(User user, HashMap<String, CommandModel> commandsToBeFiltered, String searchString)
    {
        List<String> filteredList = new ArrayList<>();

        for (Map.Entry<String, CommandModel> command : commandsToBeFiltered.entrySet())
        {
            // Checks if the command starts with the word
            if( command.getKey().startsWith(searchString))
            {
                //  Checks if the user has permission for the command
                if( command.getValue() == null || CraftEraSuiteCore.userHasPermission(user, command.getValue().getPermission()) )
                {
                    filteredList.add(command.getKey());
                }
            }
        }

        return filteredList;
    }
}
