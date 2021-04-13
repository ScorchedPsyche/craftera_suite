package modules.com.github.scorchedpsyche.craftera_suite.modules.listeners;

import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitSpectatorListener implements Listener {
    private SpectatorModeManager spectatorManager;

    public PlayerQuitSpectatorListener(SpectatorModeManager spectatorManager)
    {
        this.spectatorManager = spectatorManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        spectatorManager.playerLogout(e.getPlayer());
    }
}