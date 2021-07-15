package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.core.CoreCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.games.GamesCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.sleep.SleepCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.spectator_mode.SpectatorModeCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
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
        User user;

        // Check if sender is player
        if (sender instanceof Player) {
            // Check if /CES command
            if ( isCesCommandWithArgs(command, args) ) {
                String[] argsFiltered = filterSubcommandArgs(args);

                switch( args[0] )
                {
                    case "core":
                        if( PlayerUtil.hasPermission(((Player)sender), SuitePluginManager.Core.Permissions.core) )
                        {
                            if ( argsFiltered == null )
                            {
                                argsFiltered = new String[1];
                                argsFiltered[0] = "";
                            }

                            Bukkit.getPluginManager().callEvent(new CoreCommandEvent((Player) sender, argsFiltered));
                        }

                        return true;

                    case "games":
                        if( PlayerUtil.hasPermission(((Player)sender), SuitePluginManager.Games.Permissions.games) )
                        {
                            if ( argsFiltered == null )
                            {
                                argsFiltered = new String[1];
                                argsFiltered[0] = "";
                            }

                            Bukkit.getPluginManager().callEvent(new GamesCommandEvent((Player) sender, argsFiltered));
                        }

                        return true;

                    case "hud":
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "toggle";
                        }

                        Bukkit.getPluginManager().callEvent(new HudCommandEvent((Player) sender, argsFiltered));

                        return true;

                    case "seasons":
                        if( PlayerUtil.hasPermission(((Player)sender), SuitePluginManager.Seasons.Permissions.seasons) )
                        {
                            if ( argsFiltered == null )
                            {
                                argsFiltered = new String[1];
                                argsFiltered[0] = "help";
                            }

                            Bukkit.getPluginManager().callEvent(new SeasonsCommandEvent((Player) sender, argsFiltered));
                        }

                        return true;

                    case "sleep":
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "";
                        }

                        Bukkit.getPluginManager().callEvent(new SleepCommandEvent((Player) sender, argsFiltered));

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
