package com.github.scorchedpsyche.craftera_suite.modules.util;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadUtil
{
    /**
     * Creates a temporary inventory to place the Player Heads in so that the textures are preloaded by the server.
     */
    public static void preloadPlayerHeads()
    {
        ConsoleUtil.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                "Asynchronous Player Head preload STARTED");

        int nbrOfHeadsLoaded = 0;
        int currentSlot = 0;
        Inventory inv = Bukkit.createInventory(null, 54, "Player Head Cache");

        for(OfflinePlayer player : Bukkit.getWhitelistedPlayers())
        {
            // Loops through inventory slot if there's more than 54 heads
            if( currentSlot == 54)
            {
                currentSlot = 0;
            }

            inv.setItem(currentSlot, playerHeadItemStackFromOfflinePlayer(1, player));
            nbrOfHeadsLoaded++;
            currentSlot++;
        }

        ConsoleUtil.logMessage(SuitePluginManager.WanderingTrades.Name.full,
                "Asynchronous Player Head preload ENDED with " + nbrOfHeadsLoaded + " heads loaded");
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
