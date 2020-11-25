package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CustomCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if sender is player
        if (sender instanceof Player) {
            // Check if /CES command
            if ( isCesCommandWithArgs(command, args) ) {
                String[] argsFiltered = filterSubcommandArgs(args);

                switch( args[0] )
                {
                    case "hud":
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "toggle";
                        }

                        Bukkit.getPluginManager().callEvent(new HudCommandsEvent((Player) sender, argsFiltered));

                        return true;

                    case "seasons":
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "current";
                        }

                        Bukkit.getPluginManager().callEvent(new SeasonsCommandEvent((Player) sender, argsFiltered));

                        return true;

                    default: // core

                        return false;
                }
            } 
        }
        
        return false;
    }

    private boolean isCesCommandWithArgs(Command command, String[] args)
    {
        return command.getName().equalsIgnoreCase("ces")
                && args.length > 0
                && !StringUtils.isNullOrEmpty(args[0]);
    }

    @Nullable
    private String[] filterSubcommandArgs(String[] argsUnfiltered)
    {
        if( argsUnfiltered.length > 1 && !StringUtils.isNullOrEmpty(argsUnfiltered[1]) )
        {
            String[] args = new String[argsUnfiltered.length - 1];
            System.arraycopy(argsUnfiltered, 1, args, 0, argsUnfiltered.length - 1);

            return args;
        }

        return null;
    }
}
