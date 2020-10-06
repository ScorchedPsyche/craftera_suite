package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class CustomCommandExecutor implements CommandExecutor {
    public CustomCommandExecutor(CraftEraSuiteCore cesCore) {
        super();
        this.cesCore = cesCore;
//        commandManager = new CommandManager();
    }

    private CraftEraSuiteCore cesCore;
//    private CommandManager commandManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        commandManager.runCommandIfValid(sender, command, args);

        // Check if sender is player
        if (sender instanceof Player) {
            // Check if /CES command
            if ( isCesCommandWithArgs(command, args) ) {
                switch( args[0] )
                {
                    case "hud":
                        String[] argsFiltered = filterSubcommandArgs(args);
                        if ( argsFiltered == null )
                        {
                            argsFiltered = new String[1];
                            argsFiltered[0] = "toggle";
                        }

                        HudCommandsEvent hudToggleCommandEvent =
                                new HudCommandsEvent((Player) sender, argsFiltered);
                        Bukkit.getPluginManager().callEvent(hudToggleCommandEvent);

//                        if( cesCore.suitePluginManager.isHudPluginEnabled()
//                            && args.length > 1
//                            && !cesCore.stringUtils.isEmpty(args[1])
//                            && args[1].equalsIgnoreCase("config") )
//                        {
//                            sender.sendMessage("ces hud config");
//                        } else {
//                            HudCommandEvent hudToggleCommandEvent = new HudCommandEvent((Player) sender );
//                            Bukkit.getPluginManager().callEvent(hudToggleCommandEvent);
//                            sender.sendMessage("ces hud toggle");
//                        }

                        return true;

                    default:
                        return false;
                }
            } 
        }
        
        return false;
    }

    private boolean isCesCommandWithArgs(Command command, String[] args)
    {
        if( command.getName().equalsIgnoreCase("ces")
                && args.length > 0
                && !cesCore.stringUtils.isEmpty(args[0]) )
        {
            return true;
        }

        return false;
    }

    @Nullable
    private String[] filterSubcommandArgs(String[] argsUnfiltered)
    {
        if( argsUnfiltered.length > 1 && !cesCore.stringUtils.isEmpty(argsUnfiltered[1]) )
        {
            String[] args = new String[argsUnfiltered.length - 1];
            System.arraycopy(argsUnfiltered, 1, args, 0, argsUnfiltered.length - 1);

            return args;
        }
        return null;
    }
}
