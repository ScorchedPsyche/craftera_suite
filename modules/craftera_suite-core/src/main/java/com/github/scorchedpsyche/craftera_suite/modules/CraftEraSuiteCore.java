package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.PlayerManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomCommandExecutor;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.task.TitleAndSubtitleSendToPlayerTask;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.CollectionUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;

public final class CraftEraSuiteCore extends JavaPlugin {
    public ResourcesManager resourcesManager;
    public DatabaseManager databaseManager;
    public static FileConfiguration config;
    public static HashMap<String, PlayerManager> playerManagerList = new HashMap<String, PlayerManager>();

    private Integer warnPlayersOfServerRestartTask;
    private Integer restartServerTask;
    private Integer restartMinutes = 1;
    private Integer restartSeconds = 0;

    private Integer checkMemoryUsageTaskId;
    public static TitleAndSubtitleSendToPlayerTask titleAndSubtitleSendToPlayerTask;

    // Commands
    public static CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor();
    public static CustomTabCompleter customTabCompleter = new CustomTabCompleter();

    // Dependencies
    public static LuckPerms luckPerms;

    // Plugin startup logic
    @Override
    public void onEnable()
    {
        // Display console CES logo
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "    __  " + ChatColor.YELLOW + " __  " + ChatColor.BLUE + " __");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "   |    " + ChatColor.YELLOW + "|__  " + ChatColor.BLUE + "|__    "
                + ChatColor.GREEN + "Craft" + ChatColor.YELLOW + "Era ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "   |__  " + ChatColor.YELLOW + "|__  " + ChatColor.BLUE + " __|   "
                + ChatColor.BLUE + "  Suite");
        Bukkit.getConsoleSender().sendMessage("");

        try
        {
            File cesRootFolder = FolderUtil.getOrCreateCesRootFolder();

            // Check if plugin root folder exists
            if( cesRootFolder != null )
            {
                FolderUtil.setup();
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

                // Add online players to Player Manager List
                for( Player player : Bukkit.getOnlinePlayers() )
                {
                    playerManagerList.put(player.getUniqueId().toString(), new PlayerManager());
                }

                // Register "ces" command
                this.getCommand("ces").setExecutor(customCommandExecutor);
                this.getCommand("ces").setTabCompleter(customTabCompleter);

                // REPEATING TASK: check server memory usage
                if( config.getBoolean("auto_restart_on_low_memory") )
                {
                    checkMemoryUsageTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                            this, () -> checkMemoryUsage(), 0L, 100);

                }

                // REPEATING TASK: display subtitle
                if( !CollectionUtil.isNullOrEmpty(Bukkit.getOnlinePlayers()) )
                {
                    startTitleAndSubtitleSendToPlayersTaskIfNotRunning();
                }

                // Listeners
                getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
                getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

                // Load dependencies
                RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

                if (provider != null) {
                    luckPerms = provider.getProvider();
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
        if( checkMemoryUsageTaskId != null ){ Bukkit.getScheduler().cancelTask(checkMemoryUsageTaskId); }
        if( warnPlayersOfServerRestartTask != null ){ Bukkit.getScheduler().cancelTask(warnPlayersOfServerRestartTask); }
        if( restartServerTask != null ){ Bukkit.getScheduler().cancelTask(restartServerTask); }
        cancelTitleAndSubtitleSendToPlayersTaskIfRunning();
        resourcesManager = null;
        databaseManager = null;
        playerManagerList = null;
        warnPlayersOfServerRestartTask = null;
        restartServerTask = null;
        restartMinutes = null;
        restartSeconds = null;
        checkMemoryUsageTaskId = null;
        titleAndSubtitleSendToPlayerTask = null;

        // Dependencies
        luckPerms = null;

        super.onDisable();
    }
    private String mb (long s) {
        return String.format("%d (%.2f M)", s, (double)s / (1024 * 1024));
    }

    public static void sendTitleAndSubtitleToPlayers()
    {
        for( Player player : Bukkit.getOnlinePlayers() )
        {
            PlayerManager playerManager = playerManagerList.get(player.getUniqueId().toString());

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText( playerManager.subtitle.getText() ) );
            playerManager.subtitle.reset();
        }
    }

    public static void startTitleAndSubtitleSendToPlayersTaskIfNotRunning()
    {
        if( titleAndSubtitleSendToPlayerTask == null || !titleAndSubtitleSendToPlayerTask.isRunning() )
        {
            titleAndSubtitleSendToPlayerTask = new TitleAndSubtitleSendToPlayerTask(
                    SuitePluginManager.Core.Name.full,
                    "titleAndSubtitleSendToPlayerTask"
            );
            titleAndSubtitleSendToPlayerTask.runTaskTimer(CraftEraSuiteCore.getPlugin(CraftEraSuiteCore.class),
                    0L, SuitePluginManager.Core.Task.TitleAndSubtitleSendToPlayer.period);
        }
    }

    public static void cancelTitleAndSubtitleSendToPlayersTaskIfRunning()
    {
        if( titleAndSubtitleSendToPlayerTask != null && titleAndSubtitleSendToPlayerTask.isRunning() )
        {
            titleAndSubtitleSendToPlayerTask.cancel();
        }
    }

    private void checkMemoryUsage()
    {
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
            Bukkit.getScheduler().cancelTask(checkMemoryUsageTaskId);

            for( Player player : Bukkit.getOnlinePlayers() )
            {
                player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 1);
            }
            checkMemoryUsageTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                    this, () -> warnPlayersOfServerRestart(), 0L, 20);
            checkMemoryUsageTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                    this, () -> restartServer(), 1200);
        }
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

    public static boolean userHasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
