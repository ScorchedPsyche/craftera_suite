package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.MessageUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HudCommandListener implements Listener
{
    public HudCommandListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    private HudManager hudManager;
    private final StringUtils stringUtils = new StringUtils();
    private final ConsoleUtils consoleUtils = new ConsoleUtils();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final MessageUtils messageUtils = new MessageUtils();
    private final String pluginPrefix = "CES - HUD";

    @EventHandler
    public void onHudCommandEvent(HudCommandsEvent event)
    {
        String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if( args != null && args.length > 0 )
        {
            switch( args[0].toLowerCase() )
            {
                case "config": // /ces hud config
                    if( args.length > 1 )
                    {
                        switch( args[1] )
                        {
                            case "colorize":
                                if( args.length > 2  )
                                {
                                    switch( args[2] )
                                    {
                                        case "coordinates":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_coordinates);
                                            event.getPlayer().sendMessage("toggled colorize_coordinates");
                                            break;

                                        case "nether_portal_coordinates":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_nether_portal_coordinates);
                                            event.getPlayer().sendMessage("toggled colorize_nether_portal_coordinates");
                                            break;

                                        case "player_orientation":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_player_orientation);
                                            event.getPlayer().sendMessage("toggled colorize_player_orientation");
                                            break;

                                        case "server_tps":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_server_tps);
                                            event.getPlayer().sendMessage("toggled colorize_server_tps");
                                            break;

                                        case "tool_durability":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_tool_durability);
                                            event.getPlayer().sendMessage("toggled colorize_tool_durability");
                                            break;

                                        case "world_time":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.colorize_world_time);
                                            event.getPlayer().sendMessage("toggled colorize_world_time");
                                            break;

                                        default:

                                            // TODO: display help
                                            break;
                                    }
                                } else {
                                    // TODO: display help
                                }
                                break;

                            case "display_mode":
                                if( args.length > 2  )
                                {
                                    switch( args[2] )
                                    {
                                        case "compact":
                                            hudManager.setPreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.display_mode,
                                                    DatabaseTables.Hud.PlayerPreferences.DisplayMode.compact);
                                            event.getPlayer().sendMessage("display_mode compact");
                                            break;

                                        case "extended":
                                            hudManager.setPreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferences.display_mode,
                                                    DatabaseTables.Hud.PlayerPreferences.DisplayMode.extended);
                                            event.getPlayer().sendMessage("display_mode extended");
                                            break;

                                        default:
                                            // TODO: display help
                                            break;
                                    }
                                } else {
                                    // TODO: display help
                                }
                                break;

                            default:
                                // TODO: display help
                                break;
                        }
                    } else {
                        // /ces hud config HELP
                        playerUtils.sendMessageWithPluginPrefix(
                                event.getPlayer(),
                                pluginPrefix,
                                hudConfigCommandHelpMessage());
                    }
                    break;

                case "toggle": // /ces hud toggle
                    if( args.length > 1 && !stringUtils.isNullOrEmpty(args[1]) )
                    {
                        switch( args[1] )
                        {
                            case "coordinates":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.coordinates);
                                event.getPlayer().sendMessage("toggled coordinates");
                                break;

                            case "nether_portal_coordinates":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.nether_portal_coordinates);
                                event.getPlayer().sendMessage("toggled nether_portal_coordinates");
                                break;

                            case "player_orientation":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.player_orientation);
                                event.getPlayer().sendMessage("toggled player_orientation");
                                break;

                            case "plugin_commerce":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.plugin_commerce);
                                event.getPlayer().sendMessage("toggled plugin_commerce");
                                break;

                            case "plugin_spectator":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.plugin_spectator);
                                event.getPlayer().sendMessage("toggled plugin_spectator");
                                break;

                            case "server_time":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.server_time);
                                event.getPlayer().sendMessage("toggled server_time");
                                break;

                            case "server_tps":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.server_tps);
                                event.getPlayer().sendMessage("toggled server_tps");
                                break;

                            case "tool_durability":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.tool_durability);
                                event.getPlayer().sendMessage("toggled tool_durability");
                                break;

                            case "world_time":
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.world_time);
                                event.getPlayer().sendMessage("toggled world_time");
                                break;

                            default: // /ces hud toggle HELP
                                playerUtils.sendMessageWithPluginPrefix(
                                        event.getPlayer(),
                                        pluginPrefix,
                                        hudToggleCommandHelpMessage() );
                                break;
                        }
                    } else {
                        hudManager.toggleHudForPlayer( event.getPlayer() );
                    }
                    break;

                default: // /ces hud HELP
                    playerUtils.sendMessageWithPluginPrefix( event.getPlayer(), pluginPrefix, hudHelpMessage() );
                    break;
            }
        } else {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            consoleUtils.logError("onHudCommandEvent received null or empty args. Report this to the developer");
        }
    }

    /**
     * '/ces hud' command full help page.
     * @return Formatted string with full HUD help instructions
     */
    private String hudHelpMessage()
    {
        return  "Commands usage and description:" +
                messageUtils.newLine() +
                messageUtils.newLine() +
                messageUtils.formattedCommandWithDescription(
                    "/ces hud",
                    "enables/disables the HUD;\n") +
                messageUtils.formattedCommandWithDescription(
                        "/ces hud help",
                        "this help page.") +
                messageUtils.newLine() +
                messageUtils.newLine() +
                hudConfigCommandHelpMessage()+
                messageUtils.newLine() +
                messageUtils.newLine() +
                hudToggleCommandHelpMessage();
    }

    /**
     * '/ces hud config' command full help page.
     * @return Formatted string with full HUD Config help instructions
     */
    private String hudConfigCommandHelpMessage()
    {
        return  "\nSubcommands for " + messageUtils.formattedCommand("/ces hud config ...") + ":" +
                messageUtils.newLine() +
                messageUtils.newLine() +

                messageUtils.formattedCommandWithDescription(
                        "... colorize coordinates",
                        "enables color for coordinates;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... colorize nether_portal_coordinates",
                        "enables color for Nether Portal coordinates;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... colorize player_orientation",
                        "enables color for player orientation;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... colorize server_tps",
                        "20 = green, 19-15 = yellow and below 15 = red;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... colorize tool_durability",
                        "below 50 = yellow and below 25 = red;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... colorize world_time",
                        "green = villager work hours, yellow = bed can be used and red = light level " +
                                "allows monster spawning;") +
                messageUtils.newLine() +
                messageUtils.newLine() +

                messageUtils.formattedCommandWithDescription(
                        "... display_mode compact",
                        "shows as little text as possible;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... display_mode extended",
                        "shows a lot of text.");
    }


    /**
     * '/ces hud toggle' command full help page.
     * @return Formatted string with full HUD Toggle help instructions
     */
    private String hudToggleCommandHelpMessage()
    {
        return  "\nSubcommands for " + messageUtils.formattedCommand("/ces hud toggle ...") + ":" +
                messageUtils.newLine() +
                messageUtils.newLine() +

                messageUtils.formattedCommandWithDescription(
                        "...",
                        "enables/disables the HUD;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... coordinates",
                        "enables/disables player coordinates;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... nether_portal_coordinates",
                        "enables/disables Nether Portal coordinates on the opposing dimension;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... player_orientation",
                        "enables/disables player orientation (N/S/E/W,etc);\n") +
                messageUtils.formattedCommandWithDescription(
                        "... server_time",
                        "enables/disables server_time;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... server_tps",
                        "enables/disables server Ticks Per Second (performance);\n") +
                messageUtils.formattedCommandWithDescription(
                        "... tool_durability",
                        "enables/disables main/off hand tool durability;\n") +
                messageUtils.formattedCommandWithDescription(
                        "... world_time",
                        "enables/disables world time in ticks.");
    }
}
