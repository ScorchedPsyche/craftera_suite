package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
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
                        // More than 2 args, suggest no options
                        return new ArrayList<String>();
                    } else {
                        // Only 2 args

                        // Check if HUD plugin is enabled
                        if( cesCore.suitePluginManager.isHudPluginEnabled() && args[0].equalsIgnoreCase("hud") )
                        {
                            return new ArrayList<String>() {
                                private static final long serialVersionUID = 1L;
            
                                {
                                add("config");
                                add("toggle");
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
