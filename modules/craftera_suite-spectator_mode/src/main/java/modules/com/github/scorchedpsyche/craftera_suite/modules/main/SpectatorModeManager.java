package modules.com.github.scorchedpsyche.craftera_suite.modules.main;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerHeadUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerUtils;
import modules.com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteSpectatorMode;
import modules.com.github.scorchedpsyche.craftera_suite.modules.model.SpectatorPlayerDataModel;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        if (spectatorDatabaseAPI.enableSpectatorModeForPlayer(player))
        {
            player.setGameMode(GameMode.SPECTATOR);
            spawnArmorStand(player);
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Enabled");
        } else {
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Unable to go into Spectator mode! Contact server's admin if this should've worked.");
        }
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
            removeArmorStand(player);
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Disabled");
        } else {
            PlayerUtils.sendMessageWithPluginPrefix(player, SuitePluginManager.SpectatorMode.Name.compact,
                    "Unable to get out of Spectator mode! Contact server's admin if this should've worked.");
        }
    }

    private void spawnArmorStand(Player player)
    {
        ArmorStand as = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        as.getLocation().setYaw(player.getLocation().getYaw());
        as.setGravity(false);
        as.getEquipment().setHelmet(PlayerHeadUtils.playerHeadItemStackFromOfflinePlayer(1, player), true);
        as.setInvulnerable(true);
        as.setInvisible(true);

        as.getPersistentDataContainer().set(
                new NamespacedKey(CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class), "ces-sm_uuid"),
                PersistentDataType.STRING,
                player.getUniqueId().toString()
        );
    }

    private void removeArmorStand(Player player)
    {
        Collection<Entity> armorStandsOnPlayerLocation = player.getWorld().getNearbyEntities(
                player.getLocation(),
                0.5,
                1,
                0.5,
                (entity) -> entity.getType() == EntityType.ARMOR_STAND);

        if( !armorStandsOnPlayerLocation.isEmpty() )
        {
            String playerUUIDasString = player.getUniqueId().toString();
            armorStandsOnPlayerLocation.forEach(
                    as -> {
                        NamespacedKey namespacedKey = new NamespacedKey(CraftEraSuiteSpectatorMode.getPlugin(CraftEraSuiteSpectatorMode.class),"ces-sm_uuid");

                        if( as.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING).equals(playerUUIDasString) )
                        {
                            as.remove();
                        }
                    }
            );
        }
    }

    private List<ArmorStand> filterArmorStands(List<Entity> entities) {
        final List<ArmorStand> result = new ArrayList<>();
        for (Entity ent: entities) {
            if (ent.getType() == EntityType.ARMOR_STAND)
                result.add((ArmorStand)ent);
        }

        return result;
    }
}
