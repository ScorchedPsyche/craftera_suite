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
                    // TODO: config
                    break;

                default: // toggle
                    if( args.length > 1 && !stringUtils.isEmpty(args[1]) )
                    {
                        switch( args[1] )
                        {
                            case "coordinates":
                                // TODO: coordinates
                                break;

                            case "nether_portal_coordinates":
                                // TODO: nether_portal_coordinates
                                hudManager.togglePreferenceForPlayer(
                                        event.getPlayer(),
                                        DatabaseTables.Hud.PlayerPreferences.nether_portal_coordinates);
                                event.getPlayer().sendMessage("toggled nether_portal_coordinates");
                                break;

                            case "player_orientation":
                                // TODO: player_orientation
                                break;

                            case "plugin_commerce":
                                // TODO: plugin_commerce
                                break;

                            case "plugin_spectator":
                                // TODO: plugin_spectator
                                break;

                            case "server_time":
                                // TODO: server_time
                                break;

                            case "server_tps":
                                // TODO: server_tps
                                break;

                            case "tool_durability":
                                // TODO: tool_durability
                                break;

                            case "world_time":
                                // TODO: world_time
                                break;

                            default: // world_time_with_work_hours
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
