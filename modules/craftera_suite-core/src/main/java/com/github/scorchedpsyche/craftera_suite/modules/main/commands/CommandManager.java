package com.github.scorchedpsyche.craftera_suite.modules.main.commands;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.models.CommandModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandManager
{
    private HashMap<String, CommandModel> commands;

    public CommandManager(SuitePluginManager suitePluginManager)
    {
        this.suitePluginManager = suitePluginManager;
        commands = new HashMap<>();
//        setupCesCommand();
    }

    private SuitePluginManager suitePluginManager;

    public HashMap<String, CommandModel> getCommands()
    {
        return commands;
    }

    public boolean runCommandIfValid(CommandSender sender, Command command, String[] args)
    {


        return false;
    }

    private void setupCesCommand(Player sender)
    {
        commands.put("ces", new CommandModel(  null, new HashMap<>() ) );
        commands.get("ces").subCommands.putAll( getHudCommands( sender ) );
    }

    // HUD commands:
    // /ces hud toggle
    // /ces hud toggle nether_portal_coordinates
    // /ces hud toggle player_coordinates
    // /ces hud toggle player_orientation
    // /ces hud toggle plugin_commerce
    // /ces hud toggle plugin_spectator_range
    // /ces hud toggle server_time
    // /ces hud toggle tool_durability
    // /ces hud toggle world_time
    // /ces hud toggle world_time_work_hours
    public HashMap<String, CommandModel> getHudCommands(Player sender)
    {
        if( suitePluginManager.isHudPluginEnabled() )
        {
            // hud
            HashMap<String, CommandModel> hud = new HashMap<>();
            hud.put("hud", new CommandModel());

            // toggle
            HashMap<String, CommandModel> hudSubcommands = new HashMap<>();
//            hudSubcommands.put("toggle", new CommandModel(new HudCommandEvent((Player) sender )));
            HashMap<String, CommandModel> toggleSubcommands = hudSubcommands.get("toggle").subCommands;
//            toggleSubcommands.put(
//                    "nether_portal_coordinates",
//                    new CommandModel( new HudToggleCommandEvent((Player) sender ), null ));

            // copy all
            hud.get("hud").subCommands.putAll( hudSubcommands );

            return hud;
        }

        return null;
    }
}
