package com.github.scorchedpsyche.craftera_suite.modules.model;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableModel extends BukkitRunnable {
    public RunnableModel(String prefix, String name) {
        this.name = name;
        this.prefix = prefix;
        this.setStartAndCancelMessages(name);
    }

    private String prefix = SuitePluginManager.Name.full;
    private String name = "UNNAMED";

    protected String startMessage = "Task " + ChatColor.DARK_GREEN + "UNNAMED" + ChatColor.RESET + " started";
    protected String cancelMessage = "Task " + ChatColor.DARK_RED + "UNNAMED" + ChatColor.RESET + " cancelled";
    private boolean isRunning = false;

    @Override
    public void run() {

    }

    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     * Sets the task as running so that it can be checked with {@link RunnableModel#isRunning()}
     */
    protected void setAsRunning()
    {
        if(!isRunning)
        {
            isRunning = true;
            if ( CraftEraSuiteCore.config.getBoolean("log_task_debug_messages_to_console", true) )
            {
                ConsoleUtil.logMessage(this.prefix, this.startMessage);
            }
        }
    }

    /**
     * Sets the task as NOT running so that it can be checked with {@link RunnableModel#isRunning()}
     */
    @Override
    public void cancel()
    {
        if ( !isCancelled() )
        {
            cancelWithOptionalLogMessage();
            isRunning = false;
        } else {
            if ( CraftEraSuiteCore.config.getBoolean("log_task_debug_messages_to_console", true) )
            {
                ConsoleUtil.logMessage(this.cancelMessage);
            }
        }
    }

    private void setStartAndCancelMessages(String name)
    {
        this.startMessage = "Task " + ChatColor.DARK_GREEN + name + ChatColor.RESET + " started";
        this.cancelMessage = "Task " + ChatColor.DARK_RED + name + ChatColor.RESET + " cancelled";
    }

//    @NotNull
//    public synchronized BukkitTask runTaskTimerWithOptionalLogMessage(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException
//    {
////        if ( CraftEraSuiteCore.config.getBoolean("log_task_debug_messages_to_console", true) )
////        {
////            ConsoleUtils.logMessage(this.startMessage);
////        }
//        return super.runTaskTimer(plugin, delay, period);
//    }

    private void cancelWithOptionalLogMessage()
    {
        if ( CraftEraSuiteCore.config.getBoolean("log_task_debug_messages_to_console", true) )
        {
            ConsoleUtil.logMessage(this.prefix, this.cancelMessage);
        }
        super.cancel();
//        if ( CraftEraSuiteCore.config.getBoolean("log_task_debug_messages_to_console", true) )
//        {
//            ConsoleUtils.logMessage(prefix, "Task " + ChatColor.DARK_RED + name + ChatColor.RESET + " started");
//        }
//        this.cancel();
    }
}
