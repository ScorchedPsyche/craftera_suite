package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.MessageUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoreCommandListener implements Listener
{

    private final StringUtils stringUtils = new StringUtils();
    private final ConsoleUtils consoleUtils = new ConsoleUtils();
    private final PlayerUtils playerUtils = new PlayerUtils();
    private final MessageUtils messageUtils = new MessageUtils();
    private final String pluginPrefix = "CES - HUD";

    @EventHandler
    public void onCoreCommandEvent(HudCommandsEvent event)
    {
        String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if( args != null && args.length > 0 )
        {
            switch( args[0].toLowerCase() )
            {
                case "item": // /ces hud config
                    if( args.length > 1 )
                    {
                        switch( args[1] )
                        {
                            case "remove":
                                if( args.length > 2  )
                                {
                                    switch( args[2] )
                                    {
                                    }
                                } else {
                                    // TODO: display help
                                }
                                break;

                            case "display_mode":
                                if( args.length > 2  )
                                {
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
