package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandsEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.HudManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseTables;
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

    @EventHandler
    public void onHudCommandEvent(HudCommandsEvent event)
    {
        String[] args = event.getArgs();
        if( args != null && args.length > 0 )
        {
            switch( args[0] )
            {
                case "config":
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
                        // TODO: display help
                    }
                    break;

                default: // toggle
                    if( args.length > 1 && !stringUtils.isEmpty(args[1]) )
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
                        }
                    } else {
//                        hudManager.hudDatabaseAPI.toggleBooleanForPlayer(
//                                DatabaseTables.Hud.player_preferences,
//                                event.getPlayer().getUniqueId().toString(),
//                                DatabaseTables.Hud.PlayerPreferences.enabled );
                        hudManager.toggleHudForPlayer( event.getPlayer() );
                    }
                    break;
            }
        } else {
            // TODO: Display HUD subcommand help
            event.getPlayer().sendMessage("FAIL");
        }

//        hudManager.toggleHudForPlayer( event.getPlayer() );
    }
}
