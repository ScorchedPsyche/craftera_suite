package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.StringUtils;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomCommandExecutor;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;

import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEraSuiteCore extends JavaPlugin {
    public String pluginNamePrefix = "CraftEra Suite";

    public DatabaseManager db = new DatabaseManager();
    public StringUtils stringUtils;
    public FolderUtils folderUtils;
    public ConsoleUtils consoleUtils;
    public ResourcesManager resourcesManager;
    
    public SuitePluginManager suitePluginManager;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        stringUtils = new StringUtils();
        folderUtils = new FolderUtils(this);
        consoleUtils = new ConsoleUtils();
        suitePluginManager = new SuitePluginManager();
        resourcesManager = new ResourcesManager();



        // Register "ces" command
        this.getCommand("ces").setExecutor(new CustomCommandExecutor(this));
        this.getCommand("ces").setTabCompleter(new CustomTabCompleter(this));
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
    }
}
