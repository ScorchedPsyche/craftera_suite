package com.github.scorchedpsyche.craftera_suite.modules.utils;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadUtils
{
    public static void preloadPlayerHeads()
    {
        ConsoleUtils.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                                "Asynchronous Player Head preload STARTED");

        int nbrOfHeadsLoaded = 0;
        Inventory inv = Bukkit.createInventory(null, 54, "Player Head Cache");

        for(OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            inv.addItem(playerHeadItemStackFromOfflinePlayer(1, player));
            nbrOfHeadsLoaded++;
        }

        ConsoleUtils.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                                "Player Head preload ENDED with " + Math.abs(nbrOfHeadsLoaded) + " heads loaded");
        inv = null;
    }

    /***
     * Creates a Player Head by Owner ID. The head will later be retrieved asynchronously from Mojang.
     * @param amount Quantity of heads to give to the player when traded
     * @param player The UUID of the player who owns the head
     * @return An item stack of the specified player head
     */
    public static ItemStack playerHeadItemStackFromOfflinePlayer(int amount, OfflinePlayer player)
    {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, amount);

        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer( player );

        playerHead.setItemMeta( meta );

        return playerHead;
    }
}
