package modules.com.github.scorchedpsyche.craftera_suite.modules.listeners;

import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinSpectatorListener implements Listener {
    private SpectatorModeManager spectatorManager;

    public PlayerJoinSpectatorListener(SpectatorModeManager spectatorManager)
    {
        this.spectatorManager = spectatorManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        spectatorManager.playerLogin(e.getPlayer());
    }
}