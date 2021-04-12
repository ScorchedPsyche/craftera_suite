package modules.com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.model.SpectatorPlayerDataModel;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpectatorModeManager {
    public SpectatorModeManager(SpectatorDatabaseAPI spectatorDatabaseAPI) {
        this.spectatorDatabaseAPI = spectatorDatabaseAPI;
    }

    private SpectatorDatabaseAPI spectatorDatabaseAPI;

    public void toggleSpectatorModeForPlayer(Player player)
    {
        if( player.getGameMode().equals(GameMode.SPECTATOR) )
        {
            disableSpectatorModeForPlayer(player);
        } else {
            enableSpectatorModeForPlayer(player);
        }
    }

    private void enableSpectatorModeForPlayer(Player player)
    {
        spectatorDatabaseAPI.enableSpectatorModeForPlayer(player);
        player.setGameMode(GameMode.SPECTATOR);
        ConsoleUtils.logMessage("SPECTATOR ON");
    }

    private void disableSpectatorModeForPlayer(Player player)
    {
        SpectatorPlayerDataModel playerData = spectatorDatabaseAPI.disableSpectatorModeForPlayer(player);

        if( playerData != null )
        {
            player.teleport(
                    new Location(
                            player.getWorld(),
                            playerData.getX(),
                            playerData.getY(),
                            playerData.getZ(),
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch()));

            switch(playerData.getGameMode())
            {
                case "SURVIVAL":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;

                case "ADVENTURE":
                    player.setGameMode(GameMode.ADVENTURE);
                    break;

                default: // CREATIVE
                    player.setGameMode(GameMode.CREATIVE);
                    break;
            }
            ConsoleUtils.logMessage("SPECTATOR OFF");
        } else {
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    ChatColor.RED + "There was an error disabling spectator mode! Talk to your server admin.");
        }
    }
}
