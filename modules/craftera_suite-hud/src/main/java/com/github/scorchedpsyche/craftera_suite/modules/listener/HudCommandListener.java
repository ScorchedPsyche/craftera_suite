package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HudCommandListener implements Listener
{
    public HudCommandListener(HudManager hudManager)
    {
        this.hudManager = hudManager;
    }

    private final HudManager hudManager;

    @EventHandler
    public void onHudCommandEvent(HudCommandEvent event)
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
                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_coordinates);
                                            break;

                                        case "nether_portal_coordinates":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_nether_portal_coordinates);
                                            break;

                                        case "player_orientation":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_player_orientation);
                                            break;

//                                        case "server_tps":
//                                            hudManager.togglePreferenceForPlayer(
//                                                    event.getPlayer(),
//                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_server_tps);
//                                            break;

                                        case "tool_durability":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_tool_durability);
                                            break;

                                        case "world_time":
                                            hudManager.togglePreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferencesTable.colorize_world_time);
                                            break;

                                        default:
                                            sendHelpMessageToPlayer(event.getPlayer(), hudConfigColorizeCommandHelpMessage());
                                            break;
                                    }
                                } else {
                                    sendHelpMessageToPlayer(event.getPlayer(), hudConfigColorizeCommandHelpMessage());
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
                                                    DatabaseTables.Hud.PlayerPreferencesTable.display_mode,
                                                    DatabaseTables.Hud.PlayerPreferencesTable.DisplayMode.compact);
                                            break;

                                        case "extended":
                                            hudManager.setPreferenceForPlayer(
                                                    event.getPlayer(),
                                                    DatabaseTables.Hud.PlayerPreferencesTable.display_mode,
                                                    DatabaseTables.Hud.PlayerPreferencesTable.DisplayMode.extended);
                                            break;

                                        default:
                                            sendHelpMessageToPlayer(event.getPlayer(), hudConfigDisplayModeCommandHelpMessage());
                                            break;
                                    }
                                } else {
                                    sendHelpMessageToPlayer(event.getPlayer(), hudConfigDisplayModeCommandHelpMessage());
                                }
                                break;

                            case "format":
                                if( args.length > 2  )
                                {
                                    switch( args[2] )
                                    {
                                        case "world_time":
                                            if( args.length > 3  )
                                            {
                                                switch( args[3] )
                                                {
                                                    case "as24Hour":
                                                        hudManager.setPreferenceForPlayer(
                                                            event.getPlayer(),
                                                            DatabaseTables.Hud.PlayerPreferencesTable.format_world_time,
                                                            DatabaseTables.Hud.PlayerPreferencesTable.Format.WorldTime.as24h);
                                                        break;

                                                    case "asTicks":
                                                        hudManager.setPreferenceForPlayer(
                                                            event.getPlayer(),
                                                            DatabaseTables.Hud.PlayerPreferencesTable.format_world_time,
                                                            DatabaseTables.Hud.PlayerPreferencesTable.Format.WorldTime.asTicks);
                                                        break;

                                                    default:
                                                        sendHelpMessageToPlayer(event.getPlayer(), hudConfigFormatWorldTimeCommandHelpMessage());
                                                        break;
                                                }
                                            } else {
                                                sendHelpMessageToPlayer(event.getPlayer(), hudConfigFormatWorldTimeCommandHelpMessage());
                                            }
                                            break;

                                        default:
                                            sendHelpMessageToPlayer(event.getPlayer(), hudConfigFormatCommandHelpMessage());
                                            break;
                                    }
                                } else {
                                    sendHelpMessageToPlayer(event.getPlayer(), hudConfigFormatCommandHelpMessage());
                                }
                                break;

                            default:
                                sendHelpMessageToPlayer(event.getPlayer(), hudConfigCommandHelpMessage());
                                break;
                        }
                    } else {
                        sendHelpMessageToPlayer(event.getPlayer(), hudConfigCommandHelpMessage());
                    }
                    break;

                case "toggle": // /ces hud toggle
                    if( args.length > 1 && !StringUtil.isNullOrEmpty(args[1]) )
                    {
                        //                            case "server_tps":
                        //                                hudManager.togglePreferenceForPlayer(
                        //                                        event.getPlayer(),
                        //                                        DatabaseTables.Hud.PlayerPreferencesTable.server_tps);
                        //                                break;
                        switch (args[1]) {
                            case "coordinates" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.coordinates);
                            case "nether_portal_coordinates" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.nether_portal_coordinates);
                            case "player_orientation" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.player_orientation);
                            case "plugin_commerce" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.plugin_commerce);
                            case "plugin_spectator" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.plugin_spectator);
                            case "server_time" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.server_time);
                            case "tool_durability" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.tool_durability);
                            case "world_time" -> hudManager.togglePreferenceForPlayer(
                                    event.getPlayer(),
                                    DatabaseTables.Hud.PlayerPreferencesTable.world_time);
// /ces hud toggle HELP
                            default -> sendHelpMessageToPlayer(event.getPlayer(), hudToggleCommandHelpMessage());
                        }
                    } else {
                        hudManager.toggleHudForPlayer( event.getPlayer() );
                    }
                    break;

                default: // /ces hud HELP
                    sendHelpMessageToPlayer(event.getPlayer(), hudHelpMessage());
                    break;
            }
        } else {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtil.logError("onHudCommandEvent received null or empty args. Report this to the developer");
        }
    }

    /**
     * Sends a plugin prefixed help message to the player
     * @param player The player to send the help message to
     * @param helpMessage The string for the help message
     */
    private void sendHelpMessageToPlayer(Player player, String helpMessage)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Hud.Name.compact, helpMessage);
    }

    /**
     * '/ces hud' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                "/ces hud",
                "enables/disables the HUD;").nl()
            .formattedCommandWithDescription(
                "/ces hud help",
                "this help page;").nl()
            .formattedCommandWithDescription(
                "/ces hud config ...",
                "configure the HUD to your liking;").nl()
            .formattedCommandWithDescription(
                "/ces hud toggle ...",
                "show/hide specific parts of the HUD.")
            .toString();



//        return  "Commands usage and description:" +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                MessageUtil.formattedCommandWithDescription(
//                    "/ces hud",
//                    "enables/disables the HUD;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                    "/ces hud help",
//                    "this help page.") +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                hudConfigCommandHelpMessage()+
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//                hudToggleCommandHelpMessage();
    }

    /**
     * '/ces hud config' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudConfigCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud config ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                "... display_mode ...",
                "switch between compact and extended modes;").nl()
            .formattedCommandWithDescription(
                "... colorize ...",
                "colorizes specific parts of the HUD info;").nl()
            .formattedCommandWithDescription(
                "... format ...",
                "choose formatting for specific parts of the hud.")
            .toString();


//        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces hud config ...") + ":" +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize coordinates",
//                        "enables color for coordinates;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize nether_portal_coordinates",
//                        "enables color for Nether Portal coordinates;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize player_orientation",
//                        "enables color for player orientation;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize server_tps",
//                        "20 = green, 19-15 = yellow and below 15 = red;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize tool_durability",
//                        "below 50 = yellow and below 25 = red;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... colorize world_time",
//                        "green = villager work hours, yellow = bed can be used and red = light level " +
//                                "allows monster spawning;") +
//                MessageUtil.newLine() +
//                MessageUtil.newLine() +
//
//                MessageUtil.formattedCommandWithDescription(
//                        "... display_mode compact",
//                        "shows as little text as possible;\n") +
//                MessageUtil.formattedCommandWithDescription(
//                        "... display_mode extended",
//                        "shows a lot of text.");
    }

    /**
     * '/ces hud config display_mode' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudConfigDisplayModeCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud config display_mode ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                    "... compact",
                    "shows as little text as possible;").nl()
            .formattedCommandWithDescription(
                    "... extended",
                    "shows a lot of text.").nl()
            .toString();
    }

    /**
     * '/ces hud config colorize' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudConfigColorizeCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud config colorize ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                    "... coordinates",
                    "enables color for coordinates;").nl()
            .formattedCommandWithDescription(
                    "... nether_portal_coordinates",
                    "enables color for Nether Portal coordinates;").nl()
            .formattedCommandWithDescription(
                    "... player_orientation",
                    "enables color for player orientation;").nl()
            .formattedCommandWithDescription(
                    "... server_tps",
                    "20 = green, 19-15 = yellow and below 15 = red;").nl()
            .formattedCommandWithDescription(
                    "... tool_durability",
                    "below 50 = yellow and below 25 = red;").nl()
            .formattedCommandWithDescription(
                    "... world_time",
                    "green = villager work hours, yellow = bed can be used and red = light level allows monster spawning.")
            .toString();
    }

    /**
     * '/ces hud config format' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudConfigFormatCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud config format ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                    "... world_time ...",
                    "changes world time formatting.").nl()
            .toString();
    }

    /**
     * '/ces hud config format world_time' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudConfigFormatWorldTimeCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud config format ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                    "... as24Hour",
                    "displays world time in a 24h format;").nl()
            .formattedCommandWithDescription(
                    "... asTicks",
                    "displays world time as ticks.").nl()
            .toString();
    }


    /**
     * '/ces hud toggle' command full help page.
     * @return Formatted string with help instructions
     */
    private String hudToggleCommandHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces hud toggle ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                    "... coordinates",
                    "show/hide player coordinates;").nl()
            .formattedCommandWithDescription(
                    "... nether_portal_coordinates",
                    "show/hide Nether Portal coordinates on the opposing dimension;").nl()
            .formattedCommandWithDescription(
                    "... player_orientation",
                    "show/hide player orientation (N/S/E/W,etc);").nl()
            .formattedCommandWithDescription(
                    "... server_time",
                    "show/hide server_time;").nl()
            .formattedCommandWithDescription(
                    "... tool_durability",
                    "show/hide main/off hand tool durability;").nl()
            .formattedCommandWithDescription(
                    "... world_time",
                    "show/hide world time in ticks.")
            .toString();
    }
}
