package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomCommandExecutor;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.utils.natives.FolderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

public final class CraftEraSuiteCore extends JavaPlugin {
    public ResourcesManager resourcesManager;
    public DatabaseManager databaseManager;
    public static FileConfiguration config;

    private Integer checkMemoryUsageTask;
    private Integer warnPlayersOfServerRestartTask;
    private Integer restartServerTask;
    private Integer restartMinutes = 1;
    private Integer restartSeconds = 0;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        try
        {
            File cesRootFolder = FolderUtils.getOrCreateCesRootFolder();

            // Check if plugin root folder exists
            if( cesRootFolder != null )
            {
                FolderUtils.setup();
                resourcesManager = new ResourcesManager();

                resourcesManager.copyResourcesToServer(this, cesRootFolder, new ArrayList<String>(){{
                    add("config.yml");
                }});

                config = new YamlConfiguration();
                config.load(cesRootFolder + File.separator + "config.yml");

                if ("mysql".equals(config.getString("storage_type")))
                {
                    databaseManager = new DatabaseManager(DatabaseManager.DatabaseType.MySQL);
                } else
                {
                    databaseManager = new DatabaseManager(DatabaseManager.DatabaseType.SQLite);
                }

                // Register "ces" command
                this.getCommand("ces").setExecutor(new CustomCommandExecutor());
                this.getCommand("ces").setTabCompleter(new CustomTabCompleter());

                // Set up repeating task to check server memory usage
                if( config.getBoolean("auto_restart_on_low_memory") )
                {
                    checkMemoryUsageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                            this, () -> checkMemoryUsage(), 0L, 20);

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            this.onDisable();
        }
    }

    // Plugin shutdown logic
    @Override
    public void onDisable()
    {
        if( checkMemoryUsageTask != null ){ Bukkit.getScheduler().cancelTask(checkMemoryUsageTask); }
        if( warnPlayersOfServerRestartTask != null ){ Bukkit.getScheduler().cancelTask(warnPlayersOfServerRestartTask); }
        if( restartServerTask != null ){ Bukkit.getScheduler().cancelTask(restartServerTask); }
        databaseManager = null;
        resourcesManager = null;
    }
    private String mb (long s) {
        return String.format("%d (%.2f M)", s, (double)s / (1024 * 1024));
    }

    private void checkMemoryUsage()
    {
//        long heapSize = Runtime.getRuntime().totalMemory();
//        long max = Runtime.getRuntime().maxMemory();
//
//        StringBuilder message = new StringBuilder();
//
//        message.append("Heap Size = ").append(mb(heapSize)).append("\n");
//        message.append("Heap Size = ").append(mb(heapSize)).append("\n");
//        message.append("Max Heap Size = ").append(mb(max)).append("\n");
//
//        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
//            String name = pool.getName();
//            MemoryType type = pool.getType();
//            MemoryUsage usage = pool.getUsage();
//            MemoryUsage peak = pool.getPeakUsage();
//            message.append("Heap named '").append(name);
//            message.append("' (").append(type.toString()).append(") ");
//            message.append("uses ").append(mb(usage.getUsed()));
//            message.append(" of ").append(mb(usage.getMax()));
//            message.append(". The max memory used so far is ");
//            message.append(mb(peak.getUsed())).append(".\n");
//        }
//        System.out.println(message.toString());



//        System.out.println("==============================================================");
//        System.out.println("Runtime max: " + mb(Runtime.getRuntime().maxMemory()));
//        MemoryMXBean m = ManagementFactory.getMemoryMXBean();
//
//        System.out.println("Non-heap: " + mb(m.getNonHeapMemoryUsage().getMax()));
//        System.out.println("Heap: " + mb(m.getHeapMemoryUsage().getMax()));
//
//        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
//            System.out.println("Pool: " + mp.getName() +
//                                       " (type " + mp.getType() + ")" +
//                                       " = " + mb(mp.getUsage().getMax()));
//        }
        long max = ManagementFactory.getMemoryPoolMXBeans().get(3).getUsage().getMax() +
                ManagementFactory.getMemoryPoolMXBeans().get(4).getUsage().getMax()*2 +
                ManagementFactory.getMemoryPoolMXBeans().get(5).getUsage().getMax();
        long used = ManagementFactory.getMemoryPoolMXBeans().get(3).getUsage().getUsed() +
                        ManagementFactory.getMemoryPoolMXBeans().get(4).getUsage().getUsed()*2 +
                        ManagementFactory.getMemoryPoolMXBeans().get(5).getUsage().getUsed();
        double percentage = ((double)used/max)*100;

        if( percentage >= 90 )
        {
            System.out.println( percentage + "% - " + mb(used) + "/" + mb(max) );
            Bukkit.getScheduler().cancelTask(checkMemoryUsageTask);

            for( Player player : Bukkit.getOnlinePlayers() )
            {
                player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 1);
            }
            checkMemoryUsageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                    this, () -> warnPlayersOfServerRestart(), 0L, 20);
            checkMemoryUsageTask = Bukkit.getScheduler().scheduleSyncDelayedTask(
                    this, () -> restartServer(), 1200);
        }


//        long RAM_TOTAL = Runtime.getRuntime().maxMemory();
//        long RAM_USED = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        long RAM_FREE = Runtime.getRuntime().maxMemory() - RAM_USED;
//        long RAM_TOTAL_MB = RAM_TOTAL / 1024 / 1024;
//        long RAM_FREE_MB = RAM_FREE  / 1024 / 1024;
//        long RAM_USED_MB = RAM_USED / 1024 / 1024;
//        double RAM_USED_PERCENTAGE = ((RAM_USED * 1.0) / RAM_TOTAL) * 100;
//        ConsoleUtils.logMessage(RAM_TOTAL_MB + "MB TOTAL / " + RAM_FREE_MB + "MB FREE / " + RAM_USED_MB + "MB USED (" + RAM_USED_PERCENTAGE + "%)");
//        if( RAM_USED_PERCENTAGE >= 95  )
//        {
//            for( Player player : Bukkit.getOnlinePlayers() )
//            {
//                player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 1);
//            }
//            checkMemoryUsageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
//                    this, () -> warnPlayersOfServerRestart(), 0L, 20);
//            checkMemoryUsageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
//                    this, () -> restartServer(), 0L, 200);
//        }
    }

    private void warnPlayersOfServerRestart()
    {
        if( restartSeconds == 0 && restartMinutes == 0)
        {
            restartServer();
        } else {
            for( Player player : Bukkit.getOnlinePlayers() )
            {
                player.sendTitle("Restart in " + ChatColor.GREEN + restartMinutes + ":" + restartSeconds,
                                 ChatColor.RED + "" + "Server memory high. Stop what you're doing!",
                                 0, 40, 0);
                if( restartSeconds <= 10 )
                {
                    player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
                } else {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                }
            }

            if( restartSeconds == 0 )
            {
                restartSeconds = 59;
                restartMinutes--;
//                ConsoleUtils.logError("- min");
            } else {
                restartSeconds--;
//                ConsoleUtils.logError("- sec");
            }
        }
    }

    private void restartServer()
    {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }
}
