package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.games.GamesCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.GamesManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.MessageUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GamesCommandListener implements Listener
{
    public GamesCommandListener(GamesManager gamesManager)
    {
        this.gamesManager = gamesManager;
    }

    private final GamesManager gamesManager;

    @EventHandler
    public void onEventsCommandEvent(GamesCommandEvent event) {
        String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if (args != null && args.length > 0 && event.getPlayer() != null)
        {
            gamesManager.playerExecutingCommand = event.getPlayer();
            gamesManager.playerUUID = event.getPlayer().getUniqueId().toString();

            switch (args[0].toLowerCase())
            {
                case "prepare":
                    if (args.length > 1)
                    {
                        switch (args[1])
                        {
                            case "Raid__EnderDragon":
                                gamesManager.prepareGame(SuitePluginManager.Games.Type.Raid__EnderDragon);
                                break;

                            case "Raid__EnderDragon_Chaotic":
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Games.Name.compact,
                                        "Raid__EnderDragon_Chaotic: Not Yet Implemented!");
                                break;

                            default:
                                sendHelpMessageToPlayer(event.getPlayer(), gamesPrepareCommandHelpMessage());
                                break;
                        }
                    } else {
                        sendHelpMessageToPlayer(event.getPlayer(), gamesPrepareCommandHelpMessage());
                    }
                    break;

                case "cancel":
                    gamesManager.cancelGame();
                    break;

                case "open_subscriptions":
                    gamesManager.openSubscriptions();
                    break;

                case "announce":
                    gamesManager.announce();
                    break;

                case "join":
                    // Check if target player is valid
                    if ( args.length > 1 )
                    {
                        Player targetPlayer = Bukkit.getPlayer(args[1]);
                        if( targetPlayer != null )
                        {
                            gamesManager.join(targetPlayer);
                        } else {
                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Games.Name.compact,
                                    ChatColor.RED + "Player not found!");
                        }
                    } else {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Games.Name.compact,
                            ChatColor.RED + "You must specify a player!");
                    }
                    break;

                case "start":
                    int countdownInSeconds = 5;
                    if ( args.length > 1 )
                    {
                        try
                        {
                            countdownInSeconds = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e)
                        {
                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Games.Name.compact,
                                ChatColor.RED + "Argument after start must be an integer (in seconds)");
                            return;
                        }
                    }

                    gamesManager.start(countdownInSeconds);
                    break;

                default:
                    sendHelpMessageToPlayer(event.getPlayer(), ganessCommandHelpMessage());
                    break;
            }
        } else
        {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtil.logError("onSeasonsCommandEvent received null or empty args. Report this to the developer");
        }
    }

    /**
     * Sends a plugin prefixed help message to the player
     * @param player The player to send the help message to
     * @param helpMessage The string for the help message
     */
    private void sendHelpMessageToPlayer(Player player, String helpMessage)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Games.Name.compact, helpMessage);
    }

    /**
     * '/ces games' command full help page.
     * @return Formatted string with full help instructions
     */
    private String ganessCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces games ...") + ":" +
                MessageUtil.newLine() + MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n");
    }

    /**
     * '/ces games prepare' command full help page.
     * @return Formatted string with full help instructions
     */
    private String gamesPrepareCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces games prepare...") + ":" +
                MessageUtil.newLine() + MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n");
    }
}
