package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.models.CommandModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CustomTabCompleter implements TabCompleter {
    private CraftEraSuiteCore cesCore;
    private CommandsManager commandManager;
    private HashMap<String, CommandModel> commands;

    public CustomTabCompleter(CraftEraSuiteCore cesCore) {
        super();
        this.cesCore = cesCore;

        commands = new HashMap<>();

        commands.putAll(setupHudCommands());


//        System.out.println(hudConfig.subCommands.get("config"));
//        System.out.println(hudConfig.subCommands.get("config").subCommands.get("display_mode"));
//        System.out.println(hudConfig.subCommands.get("config").subCommands.get("display_mode").subCommands.get(
//                "compact"));
    }

    private HashMap<String, CommandModel> setupHudCommands()
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

//        Iterator it = commands.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//        }

        // Check if there's a valid argument
        if( args.size() > 0 && !cesCore.stringUtils.isNullOrEmpty(args.get(0)) )
        {
            if( commands.containsKey(args.get(0)) )
            {
                HashMap<String, CommandModel> subCommands = commands.get(args.get(0)).subCommands;

                if( args.size() > 1 )
                {
                    args.remove(0);
                    return getSubcommands(subCommands, args);
                }
            }
        }
        return new ArrayList<String>(commands.keySet());
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






//                if( args.length == 0 || (args.length > 0 && cesCore.stringUtils.isNullOrEmpty(args[0])) )
//                {
//                    List<String> subCommands = new ArrayList<>();
//
//                    subCommands.add("core");
//
//                    // Check if HUD is enabled
//                    if( cesCore.suitePluginManager.isHudPluginEnabled() )
//                    {
//                        subCommands.add("hud");
//                    }
//
//                    return subCommands;
//                } else {
//                    // args.length > 0. CES subcommands
//
//                    if( !cesCore.stringUtils.isNullOrEmpty(args[1]) )
//                    {
//                        switch( args[1] )
//                        {
//                            case "hud":
//                                // Check if HUD is enabled
//                                if( cesCore.suitePluginManager.isHudPluginEnabled() )
//                                {
//                                    // HUD subcommands
//
//                                    //  HUD subcommand. Return suggestions
//                                    return new ArrayList<String>() {
//                                        private static final long serialVersionUID = 1L;
//
//                                        {
//                                            add("config");
//                                            add("toggle");
//                                            add("help");
//                                        }};
//                                }
//                                break;
//
//                            default: // core
//                                break;
//                        }
//                    }
//
//
//                    // Check if more than 2 args
//                    if ( args.length > 2 )
//                    {
//                        // HUD ... subcommand. More than 2 args
//
//                        if( args[1].equalsIgnoreCase("toggle") )
//                        {
//                            // HUD TOGGLE subcommand
//                            List<String> subCommands = new ArrayList<>();
//
//                            subCommands.add("coordinates");
//                            subCommands.add("nether_portal_coordinates");
//                            subCommands.add("player_orientation");
//                            if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-commerce") )
//                            {
//                                subCommands.add( "plugin_commerce");
//                            }
//                            if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-spectator") )
//                            {
//                                subCommands.add("plugin_spectator");
//                            }
//                            subCommands.add("server_time");
//                            subCommands.add("server_tps");
//                            subCommands.add("tool_durability");
//                            subCommands.add("world_time");
//
//                            return subCommands;
//                        } else if( args[1].equalsIgnoreCase("config") )
//                        {
//                            // HUD CONFIG subcommand
//
//                            // Check if more than 3 args
//                            if ( args.length > 3 )
//                            {
//                                // HUD CONFIG ... subcommand. More than 3 args
//                                List<String> subCommands = new ArrayList<>();
//
//                                if( args[2].equalsIgnoreCase("display_mode") )
//                                {
//                                    //  HUD CONFIG DISPLAY_MODE subcommand
//                                    subCommands.add("compact");
//                                    subCommands.add("extended");
//                                } else if ( args[2].equalsIgnoreCase("colorize") )
//                                {
//                                    //  HUD CONFIG COLORIZE subcommand
//                                    subCommands.add("coordinates");
//                                    subCommands.add("nether_portal_coordinates");
//                                    subCommands.add("player_orientation");
//                                    subCommands.add("server_tps");
//                                    subCommands.add("tool_durability");
//                                    subCommands.add("world_time");
//                                }
//
//                                return subCommands;
//                            }
//
//                            // HUD CONFIG subcommand. Return suggestions
//                            List<String> subCommands = new ArrayList<>();
//
//                            subCommands.add("display_mode");
//                            subCommands.add("colorize");
//
//                            return subCommands;
//                        }
//
//                        return new ArrayList<String>();
//                    } else {
//                        // Only 2 args
//
//                        // Check if HUD plugin is enabled
//                        if( cesCore.suitePluginManager.isHudPluginEnabled() && args[0].equalsIgnoreCase("hud") )
//                        {
//                        } else {
//                            // No CES plugins are enabled. Return no options
//                            return new ArrayList<String>();
//                        }
//                    }
//                }
            } 
        }
        
        return null;
    }
    
}
