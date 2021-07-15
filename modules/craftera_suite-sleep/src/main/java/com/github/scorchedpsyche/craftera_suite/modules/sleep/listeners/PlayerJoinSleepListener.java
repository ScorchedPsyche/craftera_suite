//package com.github.scorchedpsyche.craftera_suite.modules.sleep.listeners;
//
//import com.github.scorchedpsyche.craftera_suite.modules.sleep.main.SleepManager;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//
//public final class PlayerJoinSleepListener implements Listener {
//    private final SleepManager sleepManager;
//
//    public PlayerJoinSleepListener(SleepManager sleepManager)
//    {
//        this.sleepManager = sleepManager;
//    }
//
//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent e)
//    {
//        sleepManager.playerJoinedTheGame(e.getPlayer());
//    }
//}