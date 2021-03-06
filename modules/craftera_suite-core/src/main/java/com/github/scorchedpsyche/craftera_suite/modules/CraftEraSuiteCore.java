package com.github.scorchedpsyche.craftera_suite.modules;

import com.github.scorchedpsyche.craftera_suite.modules.listener.CoreCommandListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerJoinListener;
import com.github.scorchedpsyche.craftera_suite.modules.listener.PlayerQuitListener;
import com.github.scorchedpsyche.craftera_suite.modules.main.PlayerManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.ResourcesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.ServerManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomCommandExecutor;
import com.github.scorchedpsyche.craftera_suite.modules.main.commands.CustomTabCompleter;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.CoreDatabaseApi;
import com.github.scorchedpsyche.craftera_suite.modules.main.database.DatabaseManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.CommandModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.task.TitleAndSubtitleSendToPlayerTask;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.CollectionUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.FolderUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class CraftEraSuiteCore extends JavaPlugin {
    public ResourcesManager resourcesManager;
    public DatabaseManager databaseManager;
    public static FileConfiguration config;
    public static HashMap<String, PlayerManager> playerManagerList = new HashMap<>();
    public static ServerManager serverManager;
    private CoreDatabaseApi coreDatabaseApi;

    // Player List Header
    private final StringFormattedModel baseHeaderStringFormattedModel = new StringFormattedModel();
    private String baseHeaderStr;

    private Integer warnPlayersOfServerRestartTask;
    private Integer restartServerTask;
    private Integer restartMinutes = 1;
    private Integer restartSeconds = 0;

    private Integer checkMemoryUsageTaskId;
    private Integer updatePlayerListHeaderTaskId;
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

                coreDatabaseApi = new CoreDatabaseApi();

                // Setup and verify DB tables
                if( coreDatabaseApi.setupAndVerifySqlTable() )
                {
                    // Initialize and configure
                    serverManager = new ServerManager(coreDatabaseApi)
                            .loadAndVerifyServerMessages();
                    baseHeaderStr = baseHeaderStringFormattedModel.add(Bukkit.getServer().getMotd()).nl().nl().toString();

                    // Add online players to Player Manager List
                    for( Player player : Bukkit.getOnlinePlayers() )
                    {
                        playerManagerList.put(player.getUniqueId().toString(), new PlayerManager(player));
                    }

                    // Register "ces" command
                    try {
                        Objects.requireNonNull(this.getCommand("ces")).setExecutor(customCommandExecutor);
                        Objects.requireNonNull(this.getCommand("ces")).setTabCompleter(customTabCompleter);
                    } catch(NullPointerException e){
                        ConsoleUtil.logError(e.getMessage());
                    }

                    // Add plugin commands
                    addPluginCommands();

                    // REPEATING TASK: update Player List Header
                    updatePlayerListHeaderTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                            this, this::updatePLayerListHeader, 0L, 20);

                    // REPEATING TASK: check server memory usage
                    if( config.getBoolean("auto_restart_on_low_memory") )
                    {
                        checkMemoryUsageTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                                this, this::checkMemoryUsage, 0L, 100);

                    }

                    // REPEATING TASK: display subtitle
                    if( !CollectionUtil.isNullOrEmpty(Bukkit.getOnlinePlayers()) )
                    {
                        startTitleAndSubtitleSendToPlayersTaskIfNotRunning();
                    }

                    // Listeners
                    getServer().getPluginManager().registerEvents(new CoreCommandListener(serverManager), this);
                    getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
                    getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

                    // Load dependencies
                    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

                    if (provider != null) {
                        luckPerms = provider.getProvider();
                    }
                } else {
                    // Failed to create database tables! Display error and disable plugin
                    ConsoleUtil.logError(this.getName(), "Failed to create database tables. Disabling!");
                    Bukkit.getPluginManager().disablePlugin(this);
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
        if( updatePlayerListHeaderTaskId != null ){ Bukkit.getScheduler().cancelTask(updatePlayerListHeaderTaskId); }
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
        updatePlayerListHeaderTaskId = null;
        titleAndSubtitleSendToPlayerTask = null;

        // Dependencies
        luckPerms = null;

        super.onDisable();
    }

    private void addPluginCommands()
    {
        HashMap<String, CommandModel> messagesSubcommands = new HashMap<>();
        messagesSubcommands.put("new", new CommandModel());

        HashMap<String, CommandModel> serverSubcommands = new HashMap<>();
        serverSubcommands.put("messages", new CommandModel().addSubcommands(messagesSubcommands));

        HashMap<String, CommandModel> coreSubcommands = new HashMap<>();
        coreSubcommands.put("server", new CommandModel().addSubcommands(serverSubcommands));

        HashMap<String, CommandModel> events = new HashMap<>();
        events.put("core", new CommandModel(SuitePluginManager.Core.Permissions.core).addSubcommands(coreSubcommands));

        CustomTabCompleter.commands.putAll(events);
    }

    public void updatePLayerListHeader()
    {
        StringFormattedModel headerStr = new StringFormattedModel().add(baseHeaderStr).gray("TPS: ");
        int tpsLast1m = (int) MinecraftServer.getServer().recentTps[0];
        String tpsStr = String.valueOf(tpsLast1m); // last 1m
        int msptLast1m = (int) (1000 / MinecraftServer.getServer().recentTps[0]);
        String msptStr = String.valueOf(msptLast1m); // last 1m

        if( tpsLast1m == 20  ) // Very good
        {
            headerStr.green(tpsStr).gray(" MSPT: ").green(msptStr);
        } else if( tpsLast1m >= 19  ) { // Good
            headerStr.darkGreen(tpsStr).gray(" MSPT: ").darkGreen(msptStr);
        } else if( tpsLast1m >= 18  ) { // Acceptable
            headerStr.yellow(tpsStr).gray(" MSPT: ").yellow(msptStr);
        } else if( tpsLast1m >= 17  ) { // Bad
            headerStr.gold(tpsStr).gray(" MSPT: ").gold(msptStr);
        } else { // Terrible
            headerStr.red(tpsStr).gray(" MSPT: ").red(msptStr);
        }

        for( Player player : Bukkit.getOnlinePlayers() )
        {
            player.setPlayerListHeader(headerStr.nl().toString());
        }
    }

    private String mb (long s) {
        return String.format("%d (%.2f M)", s, (double)s / (1024 * 1024));
    }

    public static void sendTitleAndSubtitleToPlayers()
    {
        for( Player player : Bukkit.getOnlinePlayers() )
        {
            PlayerManager playerManager = playerManagerList.get(player.getUniqueId().toString());

            if( !StringUtil.isNullOrEmpty(playerManager.subtitle.getText()) )
            {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText( playerManager.subtitle.getText() ) );
                playerManager.subtitle.reset();
            }

            if( playerManager.titleSubtitleManager.titleSubtitleModel != null )
            {
                player.sendTitle(
                        playerManager.titleSubtitleManager.titleSubtitleModel.title.toString(),
                        playerManager.titleSubtitleManager.titleSubtitleModel.subtitle.toString(),
                        playerManager.titleSubtitleManager.titleSubtitleModel.getFadeIn(),
                        playerManager.titleSubtitleManager.titleSubtitleModel.getStay(),
                        playerManager.titleSubtitleManager.titleSubtitleModel.getFadeOut()
                );
                playerManager.titleSubtitleManager.titleSubtitleModel = null;
            }
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
                    this, this::warnPlayersOfServerRestart, 0L, 20);
            checkMemoryUsageTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                    this, this::restartServer, 1200);
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

    @NotNull
    public static boolean userHasPermission(User user, @Nullable String permission) {
        // If string is null or empty then no permission is required and any user has the permission
        if( StringUtil.isNullOrEmpty(permission) )
        {
            return true;
        }

        // If string is not null then check for permission on Luck Perms
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
