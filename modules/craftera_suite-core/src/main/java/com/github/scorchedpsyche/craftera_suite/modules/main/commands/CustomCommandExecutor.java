package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.spectator_mode.SpectatorModeCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

                        Bukkit.getPluginManager().callEvent(new HudCommandEvent((Player) sender, argsFiltered));

                        return true;

                    case "seasons":
                        User user = CraftEraSuiteCore.luckPerms.getUserManager().getUser(((Player) sender).getUniqueId());
                        if(user != null && user.getCachedData().getPermissionData().checkPermission(SuitePluginManager.Seasons.Permissions.seasons).asBoolean())
                        {
                            if ( argsFiltered == null )
                            {
                                argsFiltered = new String[1];
                                argsFiltered[0] = "current";
                            }

                            Bukkit.getPluginManager().callEvent(new SeasonsCommandEvent((Player) sender, argsFiltered));
                        } else {
                            PlayerUtil.sendMessageWithPluginPrefix(((Player) sender).getPlayer(),SuitePluginManager.Seasons.Name.compact,
                                    ChatColor.RED + "Unauthorized.");
                        }

                        return true;

                    case "spectator":
                    case "spec":
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "toggle";
                        }

                        Bukkit.getPluginManager().callEvent(new SpectatorModeCommandEvent((Player) sender, argsFiltered));

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
                && !StringUtil.isNullOrEmpty(args[0]);
    }

    @Nullable
    private String[] filterSubcommandArgs(String[] argsUnfiltered)
    {
        if( argsUnfiltered.length > 1 && !StringUtil.isNullOrEmpty(argsUnfiltered[1]) )
        {
            String[] args = new String[argsUnfiltered.length - 1];
            System.arraycopy(argsUnfiltered, 1, args, 0, argsUnfiltered.length - 1);

            return args;
        }

        return null;
    }
}
