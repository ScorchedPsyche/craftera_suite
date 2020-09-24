package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import java.util.ArrayList;
import java.util.List;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
                    return new ArrayList<String>() {
                        private static final long serialVersionUID = 1L;
    
                        {
                        add("hud");
                    }};
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
                        if( cesCore.suitePlugins.isHudPluginEnabled && args[0].equalsIgnoreCase("hud") )
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
