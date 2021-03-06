package com.github.scorchedpsyche.craftera_suite.entities.baby;

import com.github.scorchedpsyche.craftera_suite.entities.baby.listeners.EntityDeathListener;
import com.github.scorchedpsyche.craftera_suite.entities.baby.listeners.EntityDropItemListener;
import com.github.scorchedpsyche.craftera_suite.entities.baby.listeners.EntityNamingListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteBabyEntities extends JavaPlugin {

    // Plugin startup logic
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityNamingListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
    }

    // Plugin shutdown logic
    @Override
    public void onDisable() {

        super.onDisable();
    }
}
