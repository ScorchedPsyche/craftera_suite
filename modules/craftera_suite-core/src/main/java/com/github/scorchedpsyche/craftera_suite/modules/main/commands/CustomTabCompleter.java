package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.models.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomTabCompleter implements TabCompleter {
    private CraftEraSuiteCore cesCore;
    private CommandsManager commandManager;
    private HashMap<String, CommandModel> commands;

    public CustomTabCompleter(CraftEraSuiteCore cesCore) {
        super();
        this.cesCore = cesCore;

        commands = new HashMap<>();

        commands.putAll(getHudCommands());
    }

    private HashMap<String, CommandModel> getHudCommands()
    {
        HashMap<String, CommandModel> hudSubcommands = new HashMap<>();

        hudSubcommands.put("config", new CommandModel()
                .addCommand("display_mode")
                    .addSubcommand("compact")
                    .addSubcommand("extended")
                .addCommand("colorize")
                    .addSubcommand("coordinates")
                    .addSubcommand("nether_portal_coordinates")
                    .addSubcommand("player_orientation")
                    .addSubcommand("server_tps")
                    .addSubcommand("tool_durability")
                    .addSubcommand("world_time"));

        hudSubcommands.put("toggle", new CommandModel()
                .addCommand("coordinates")
                .addCommand("nether_portal_coordinates")
                .addCommand("player_orientation"));

        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-commerce") )
        {
            hudSubcommands.get("toggle").addCommand( "plugin_commerce");
        }
        if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-spectator") )
        {
            hudSubcommands.get("toggle").addCommand( "plugin_spectator");
        }

        hudSubcommands.get("toggle")
            .addCommand("server_time")
            .addCommand("server_tps")
            .addCommand("tool_durability")
            .addCommand("world_time");

        HashMap<String, CommandModel> hud = new HashMap<>();
        hud.put("hud", new CommandModel().addSubcommands(hudSubcommands));

        return hud;
    }

    private List<String> getSubcommands(HashMap<String, CommandModel> commands, List<String> args)
    {
        // Check if there's at least a valid string for the arguments
        if( args.size() > 0 && !StringUtils.isNullOrEmpty(args.get(0)) )
        {
            // At least one valid argument

            // Check if there's exactly one argument
            if( args.size() == 1 )
            {
                // 1 arg. Return partial filtered list by word
                return filterListByPartialWord(new ArrayList<>(commands.keySet()), args.get(0));
            } else {
                // More than 1 arg. Check if current command tree contains the arg with subcommands
                if(     commands.containsKey(args.get(0)) &&
                        commands.get(args.get(0)) != null &&
                        commands.get(args.get(0)).hasSubcommands() )
                {
                    // Valid arg. Return subcommands
                    HashMap<String, CommandModel> subCommands = commands.get(args.get(0)).subCommands;

                    // More arguments. Remove parent argument and check child subcommands
                    args.remove(0);
                    return getSubcommands(subCommands, args);
                }

                // No subcommands. Suggest nothing
                return new ArrayList<>();
            }
        }

        // No valid string for argument. Return current tree position
        return new ArrayList<>(commands.keySet());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Check if sender is player
        if (sender instanceof Player) {
            // Check for CES command
            if ( command.getName().equalsIgnoreCase("ces") ) {
                // Root CES command. Return options
                if( args.length > 0  )
                {
                    List<String> arguments = new ArrayList<String>(Arrays.asList(args));

                    return getSubcommands(commands, arguments);
                } else {
                    return new ArrayList<String>(commands.keySet());
                }
            } 
        }
        
        return null;
    }

    /**
     * Returns a list filtered with words that start with the given string.
     * @param listToBeFiltered The list to search the string in
     * @param searchString The word to search for
     * @return Returns an empty list if no items that start with the provided word were found.
     */
    private List<String> filterListByPartialWord(List<String> listToBeFiltered, String searchString)
    {
        List<String> filteredList = new ArrayList<>();

        for(String str : listToBeFiltered)
        {
            if( str.startsWith(searchString) )
            {
                filteredList.add(str);
            }
        }

        return filteredList;
    }
}
