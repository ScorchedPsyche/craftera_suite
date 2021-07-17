package com.github.scorchedpsyche.craftera_suite.modules.listeners;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.sleep.SleepCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RewardsCommandListener implements Listener
{
//    public RewardsCommandListener(SleepManager sleepManager)
//    {
//        this.sleepManager = sleepManager;
//    }
//
//    private SleepManager sleepManager;

    @EventHandler
    public void onSleepCommandEvent(SleepCommandEvent event)
    {
        String[] args = event.getArgs();

        // Check if player is valid
        if ( event.getPlayer() != null )
        {
            switch (args[0].toLowerCase())
            {
                case "help":
//                    sendHelpMessageToPlayer(event.getPlayer(), sleepHelpMessage());
                    break;

                default:
                    // TODO: display player rewards
                    break;
            }
        }
    }

    private void sendHelpMessageToPlayer(Player player, String helpMessage)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Core.Name.compact, helpMessage);
    }

    /**
     * '/ces sleep' command full help page.
     * @return Formatted string with help instructions
     */
    private String sleepHelpMessage()
    {
        return new StringFormattedModel()
            .add("\nSubcommands for ").formattedCommand("/ces sleep ...").add(":").nl().add(" ").nl()
            .formattedCommandWithDescription(
                "... help",
                "displays this help page;").nl().nl()
            .formattedCommandWithDescription(
                "/ces sleep",
                new StringFormattedModel().add("Reserves the night for you and ")
                    .redR("other players cannot sleep for that night until: ").nl()
                    .aquaR("  (1) ").add("you run this command again;").nl()
                    .aquaR("  (2) ").add("the night ends;").nl()
                    .aquaR("  (3) ").add("you change worlds").add(".")
                    .toString() )
            .toString();
    }
}
