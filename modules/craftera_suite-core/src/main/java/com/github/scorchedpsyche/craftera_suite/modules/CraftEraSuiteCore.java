package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomCommandExecutor;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteCore extends JavaPlugin {
    public ResourcesManager resourcesManager;

    public DatabaseManager databaseManager;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        FolderUtils.setup(this.getDataFolder());
        resourcesManager = new ResourcesManager();

        databaseManager = new DatabaseManager(DatabaseManager.DatabaseType.SQLite);

        // Register "ces" command
        this.getCommand("ces").setExecutor(new CustomCommandExecutor());
        this.getCommand("ces").setTabCompleter(new CustomTabCompleter());
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
        databaseManager = null;
        resourcesManager = null;
    }
}
