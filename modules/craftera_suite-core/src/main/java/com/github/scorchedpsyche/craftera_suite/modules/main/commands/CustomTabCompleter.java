package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.management.remote.rmi._RMIConnection_Stub;
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
//                else {
//                    // At root command: /ces
//                    List<String> filteredList = new ArrayList<>();
//                    User user = CraftEraSuiteCore.luckPerms.getUserManager().getUser(((Player) sender).getUniqueId());
//
//                    // Check if LP's user is valid
//                    if( user != null )
//                    {
//                        // Valid LP user. Loop through commands
//                        for (Map.Entry<String, CommandModel> currentCommand : commands.entrySet())
//                        {
//                            // Check if user has permission to use the command
//                            if( user.getCachedData().getPermissionData().checkPermission(currentCommand.getValue().getPermission()).asBoolean() )
//                            {
//                                // Has permission. Add to filtered list
//                                filteredList.add(currentCommand.getKey());
//                            }
//                        }
//                    }
//
//                    return filteredList;
//                }
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
                        commands.get(args.get(0)).hasSubcommands() )
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

//        List<String> listToBeFiltered = new ArrayList<>(commands.keySet());
//
//        for(String str : listToBeFiltered)
//        {
//            if( str.startsWith(searchString) )
//            {
////                if( )
////                {
////
////                }
//                filteredList.add(str);
//            }
//        }
    }

//    private HashMap<String, CommandModel> getHudCommands()
//    {
//        HashMap<String, CommandModel> hudSubcommands = new HashMap<>();
//
//        hudSubcommands.put("config", new CommandModel()
//                .addCommand("display_mode")
//                .addSubcommand("compact")
//                .addSubcommand("extended")
//                .addCommand("colorize")
//                .addSubcommand("coordinates")
//                .addSubcommand("nether_portal_coordinates")
//                .addSubcommand("player_orientation")
//                .addSubcommand("server_tps")
//                .addSubcommand("tool_durability")
//                .addSubcommand("world_time"));
//
//        hudSubcommands.put("toggle", new CommandModel()
//                .addCommand("coordinates")
//                .addCommand("nether_portal_coordinates")
//                .addCommand("player_orientation"));
//
//        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-commerce") )
//        {
//            hudSubcommands.get("toggle").addCommand( "plugin_commerce");
//        }
//        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-spectator") )
//        {
//            hudSubcommands.get("toggle").addCommand( "plugin_spectator");
//        }
//
//        hudSubcommands.get("toggle")
//                      .addCommand("server_time")
//                      .addCommand("server_tps")
//                      .addCommand("tool_durability")
//                      .addCommand("world_time");
//
//        HashMap<String, CommandModel> hud = new HashMap<>();
//        hud.put("hud", new CommandModel().addSubcommands(hudSubcommands));
//        hudSubcommands = null;
//
//        return hud;
//    }

//    private HashMap<String, CommandModel> getSeasonsCommands()
//    {
//        HashMap<String, CommandModel> seasonsSubcommands = new HashMap<>();
//
//        seasonsSubcommands.put("create", new CommandModel());
//        seasonsSubcommands.put("current", new CommandModel());
//        seasonsSubcommands.put("end", new CommandModel());
//        seasonsSubcommands.put("manage", new CommandModel());
//        seasonsSubcommands.put("start", new CommandModel());
//        HashMap<String, CommandModel> seasons = new HashMap<>();
//        seasons.put("seasons", new CommandModel("craftera_suite.seasons").addSubcommands(seasonsSubcommands));
//        seasonsSubcommands = null;
//
//        return seasons;
//    }

//    private HashMap<String, CommandModel> getSpectatorModeCommands()
//    {
//        HashMap<String, CommandModel> spectatorSubcommands = new HashMap<>();
//
//        HashMap<String, CommandModel> spectator = new HashMap<>();
//        spectator.put("spectator", null);
//        spectator.put("spec", null);
//        spectatorSubcommands = null;
//
//        return spectator;
//    }
}
