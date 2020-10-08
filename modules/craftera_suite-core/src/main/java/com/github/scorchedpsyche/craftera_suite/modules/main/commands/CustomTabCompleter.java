package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomTabCompleter implements TabCompleter {
    private CraftEraSuiteCore cesCore;

    public CustomTabCompleter(CraftEraSuiteCore cesCore) {
        super();
        this.cesCore = cesCore;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Check if sender is player
        if (sender instanceof Player) {
            // Check for CES command
            if ( command.getName().equalsIgnoreCase("ces") ) { 
                // Root CES command. Return options
                if( args.length == 0 || (args.length > 0 && cesCore.stringUtils.isEmpty(args[0])) )
                {
                    List<String> subCommands = new ArrayList<>();

                    // Check if HUD is enabled
                    if( cesCore.suitePluginManager.isHudPluginEnabled() )
                    {
                        subCommands.add("hud");
                    }

                    return subCommands;
                } else {
                    // CES subcommand

                    // Check if more than 2 args
                    if ( args.length > 2 )
                    {
                        // HUD ... subcommand. More than 2 args

                        if( args[1].equalsIgnoreCase("toggle") )
                        {
                            // HUD TOGGLE subcommand
                            List<String> subCommands = new ArrayList<>();

                            subCommands.add("coordinates");
                            subCommands.add("nether_portal_coordinates");
                            subCommands.add("player_orientation");
                            if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-commerce") )
                            {
                                subCommands.add( "plugin_commerce");
                            }
                            if( Bukkit.getPluginManager().isPluginEnabled("craftera_suite-spectator") )
                            {
                                subCommands.add("plugin_spectator");
                            }
                            subCommands.add("server_time");
                            subCommands.add("server_tps");
                            subCommands.add("tool_durability");
                            subCommands.add("world_time");

                            return subCommands;
                        } else if( args[1].equalsIgnoreCase("config") )
                        {
                            // HUD CONFIG subcommand

                            // Check if more than 3 args
                            if ( args.length > 3 )
                            {
                                // HUD CONFIG ... subcommand. More than 3 args
                                List<String> subCommands = new ArrayList<>();

                                if( args[2].equalsIgnoreCase("display_mode") )
                                {
                                    //  HUD CONFIG DISPLAY_MODE subcommand
                                    subCommands.add("compact");
                                    subCommands.add("extended");
                                } else if ( args[2].equalsIgnoreCase("colorize") )
                                {
                                    //  HUD CONFIG COLORIZE subcommand
                                    subCommands.add("coordinates");
                                    subCommands.add("nether_portal_coordinates");
                                    subCommands.add("player_orientation");
                                    subCommands.add("server_tps");
                                    subCommands.add("tool_durability");
                                    subCommands.add("world_time");
                                }

                                return subCommands;
                            }

                            // HUD CONFIG subcommand. Return suggestions
                            List<String> subCommands = new ArrayList<>();

                            subCommands.add("display_mode");
                            subCommands.add("colorize");

                            return subCommands;
                        }

                        return new ArrayList<String>();
                    } else {
                        // Only 2 args

                        // Check if HUD plugin is enabled
                        if( cesCore.suitePluginManager.isHudPluginEnabled() && args[0].equalsIgnoreCase("hud") )
                        {
                            //  HUD subcommand. Return suggestions
                            return new ArrayList<String>() {
                                private static final long serialVersionUID = 1L;
            
                                {
                                add("config");
                                add("toggle");
                                add("help");
                            }};
                        } else {
                            // No CES plugins are enabled. Return no options
                            return new ArrayList<String>();
                        } 
                    }
                }
            } 
        }
        
        return null;
    }
    
}
