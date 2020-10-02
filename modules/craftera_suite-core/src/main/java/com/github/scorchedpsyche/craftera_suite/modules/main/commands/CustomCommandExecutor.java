package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.events.commands.hud.HudToggleCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomCommandExecutor implements CommandExecutor {
    public CustomCommandExecutor(CraftEraSuiteCore cesCore) {
        super();
        this.cesCore = cesCore;
    }

    private CraftEraSuiteCore cesCore;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is player
        if (sender instanceof Player) {
            // ces
            if (    command.getName().equalsIgnoreCase("ces") 
                    && args.length > 0 
                    && !cesCore.stringUtils.isEmpty(args[0]) ) { 
                switch( args[0] )
                {
                    case "hud":
                        if( cesCore.suitePluginManager.isHudPluginEnabled()
                            && args.length > 1 
                            && !cesCore.stringUtils.isEmpty(args[1]) 
                            && args[1].equalsIgnoreCase("config") )
                        {
                            sender.sendMessage("ces hud config");
                        } else {
                            HudToggleCommandEvent hudToggleCommandEvent = new HudToggleCommandEvent((Player) sender );
                            Bukkit.getPluginManager().callEvent(hudToggleCommandEvent);
                            sender.sendMessage("ces hud toggle");
                        }

                        return true;

                    default:
                        return false;
                }
            } 
        }
        
        return false;
    }
}
