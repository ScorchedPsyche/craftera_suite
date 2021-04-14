package modules.com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.spectator_mode.SpectatorModeCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.utils.MessageUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpectatorModeCommandListener implements Listener
{
    public SpectatorModeCommandListener(SpectatorModeManager spectatorManager)
    {
        this.spectatorManager = spectatorManager;
    }

    private final SpectatorModeManager spectatorManager;

    @EventHandler
    public void onSpectatorCommandEvent(SpectatorModeCommandEvent event)
    {
        if( event.getPlayer() != null )
        {
            spectatorManager.toggleSpectatorModeForPlayer(
                    event.getPlayer(),
                    event.getPlayer().getLocation().getX(),
                    event.getPlayer().getLocation().getY(),
                    event.getPlayer().getLocation().getZ(),
                    event.getPlayer().getHealth()
            );
        }
        /*String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if( args != null && args.length > 0 )
        {
            switch( args[0].toLowerCase() )
            {

                case "toggle": // /ces hud toggle
                    if( args.length > 1 && !StringUtils.isNullOrEmpty(args[1]) )
                    {
                        switch( args[1] )
                        {
                            case "coordinates":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.coordinates);
                                break;

                            case "nether_portal_coordinates":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.nether_portal_coordinates);
                                break;

                            case "player_orientation":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.player_orientation);
                                break;

                            case "plugin_commerce":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.plugin_commerce);
                                break;

                            case "plugin_spectator":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.plugin_spectator);
                                break;

                            case "server_time":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.server_time);
                                break;

                            case "server_tps":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.server_tps);
                                break;

                            case "tool_durability":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.tool_durability);
                                break;

                            case "world_time":
                                spectatorManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferencesTable.world_time);
                                break;

                            default: // /ces hud toggle HELP
                                PlayerUtils.sendMessageWithPluginPrefix(
                                        event.getPlayer(),
                                        SuitePluginManager.Hud.Name.compact,
                                        hudToggleCommandHelpMessage() );
                                break;
                        }
                    } else {
                        spectatorManager.toggleHudForPlayer( event.getPlayer() );
                    }
                    break;

                default: // /ces hud HELP
                    PlayerUtils.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Hud.Name.compact, hudHelpMessage());
                    break;
            }
        } else {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtils.logError("onHudCommandEvent received null or empty args. Report this to the developer");
        }*/
    }

    /**
     * '/ces hud' command full help page.
     * @return Formatted string with full HUD help instructions
     */
    private String hudHelpMessage()
    {
        return  "Commands usage and description:" +
                MessageUtils.newLine() +
                MessageUtils.newLine() +
                MessageUtils.formattedCommandWithDescription(
                    "/ces hud",
                    "enables/disables the HUD;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "/ces hud help",
                        "this help page.") +
                MessageUtils.newLine() +
                MessageUtils.newLine() +
                hudConfigCommandHelpMessage()+
                MessageUtils.newLine() +
                MessageUtils.newLine() +
                hudToggleCommandHelpMessage();
    }

    /**
     * '/ces hud config' command full help page.
     * @return Formatted string with full HUD Config help instructions
     */
    private String hudConfigCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtils.formattedCommand("/ces hud config ...") + ":" +
                MessageUtils.newLine() +
                MessageUtils.newLine() +

                MessageUtils.formattedCommandWithDescription(
                        "... colorize coordinates",
                        "enables color for coordinates;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... colorize nether_portal_coordinates",
                        "enables color for Nether Portal coordinates;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... colorize player_orientation",
                        "enables color for player orientation;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... colorize server_tps",
                        "20 = green, 19-15 = yellow and below 15 = red;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... colorize tool_durability",
                        "below 50 = yellow and below 25 = red;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... colorize world_time",
                        "green = villager work hours, yellow = bed can be used and red = light level " +
                                "allows monster spawning;") +
                MessageUtils.newLine() +
                MessageUtils.newLine() +

                MessageUtils.formattedCommandWithDescription(
                        "... display_mode compact",
                        "shows as little text as possible;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... display_mode extended",
                        "shows a lot of text.");
    }


    /**
     * '/ces hud toggle' command full help page.
     * @return Formatted string with full HUD Toggle help instructions
     */
    private String hudToggleCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtils.formattedCommand("/ces hud toggle ...") + ":" +
                MessageUtils.newLine() +
                MessageUtils.newLine() +

                MessageUtils.formattedCommandWithDescription(
                        "...",
                        "enables/disables the HUD;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... coordinates",
                        "enables/disables player coordinates;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... nether_portal_coordinates",
                        "enables/disables Nether Portal coordinates on the opposing dimension;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... player_orientation",
                        "enables/disables player orientation (N/S/E/W,etc);\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... server_time",
                        "enables/disables server_time;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... server_tps",
                        "enables/disables server Ticks Per Second (performance);\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... tool_durability",
                        "enables/disables main/off hand tool durability;\n") +
                MessageUtils.formattedCommandWithDescription(
                        "... world_time",
                        "enables/disables world time in ticks.");
    }
}
