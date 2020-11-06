package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listeners.PlayerAdvancementDoneListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftEraSuiteAchievements extends JavaPlugin
{
    @Override
    public void onEnable()
    {
//        Advancement ironOre = new Advancement(
//                new MinecraftKey("ces", "advancements.ores.iron"),
//                null,
//                new AdvancementDisplay(
//                        CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_ORE)),
//
//                ),
//
//        )

        getServer().getPluginManager().registerEvents(new PlayerAdvancementDoneListener(), this);
    }

    @Override
    public void onDisable()
    {

    }
}
