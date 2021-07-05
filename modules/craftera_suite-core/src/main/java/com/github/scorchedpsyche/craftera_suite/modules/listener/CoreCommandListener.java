package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.core.CoreCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.events.modules.hud.HudCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.ServerManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.MessageModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.CommandUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.MessageUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.mojang.brigadier.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class CoreCommandListener implements Listener
{
    public CoreCommandListener(ServerManager serverManager)
    {
        this.serverManager = serverManager;
    }

    private ServerManager serverManager;

    @EventHandler
    public void onCoreCommandEvent(CoreCommandEvent event)
    {
        String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if (args != null && args.length > 0 && event.getPlayer() != null)
        {
            switch (args[0].toLowerCase())
            {
                case "server":
                    if (args.length > 1)
                    {
                        switch (args[1].toLowerCase())
                        {
                            case "messages":
                                if (args.length > 2)
                                {
                                    switch (args[2].toLowerCase())
                                    {
                                        case "new":
                                            // Check if enough args
                                            if (args.length > 3)
                                            {
                                                if( serverManager.newServerMessage(Arrays.copyOfRange(args, 3, args.length)) )
                                                {
                                                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Core.Name.compact,
                                                        new StringFormattedModel()
                                                            .add("Message added ").greenR("successfully").add("!") .toString());
                                                } else {
                                                    sendHelpMessageToPlayer(event.getPlayer(), coreServerMessagesNewCommandHelpMessage());
                                                }
                                            } else {
                                                sendHelpMessageToPlayer(event.getPlayer(), coreServerMessagesNewCommandHelpMessage());
                                            }
                                            break;

                                        default:
                                            sendHelpMessageToPlayer(event.getPlayer(), coreServerMessagesNewCommandHelpMessage());
                                            break;
                                    }
                                } else {
                                    sendHelpMessageToPlayer(event.getPlayer(), coreServerMessagesNewCommandHelpMessage());
                                }
                                break;

                            default:
                                sendHelpMessageToPlayer(event.getPlayer(), coreServerCommandHelpMessage());
                                break;
                        }
                    } else {
                        sendHelpMessageToPlayer(event.getPlayer(), coreServerCommandHelpMessage());
                    }
                    break;

                default:
                    sendHelpMessageToPlayer(event.getPlayer(), coreCommandHelpMessage());
                    break;
            }
        } else
        {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtil.logError("onSeasonsCommandEvent received null or empty args. Report this to the developer");
        }
    }

    private void sendHelpMessageToPlayer(Player player, String helpMessage)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Core.Name.compact, helpMessage);
    }

    /**
     * '/ces core' command full help page.
     * @return Formatted string with full help instructions
     */
    private String coreCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces core ...") + ":" +
                MessageUtil.newLine() + MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n");
    }

    /**
     * '/ces core server' command full help page.
     * @return Formatted string with full help instructions
     */
    private String coreServerCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces core server ...") + ":" +
                MessageUtil.newLine() + MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n");
    }

    /**
     * '/ces core server messages' command full help page.
     * @return Formatted string with full help instructions
     */
    private String coreServerMessagesCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces core server messages ...") + ":" +
                MessageUtil.newLine() + MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n");
    }

    /**
     * '/ces core server messages new' command full help page.
     * @return Formatted string with full help instructions
     */
    private String coreServerMessagesNewCommandHelpMessage()
    {
        return new StringFormattedModel().add("\nHelp for ")
                .add(MessageUtil.formattedCommand("/ces core server messages new ...")).add(":").nl().nl()
                .aquaR(" -> ").add("The date is in the format ").yellow("day").aqua("/").yellow("month").aqua("/")
                    .yellow("year").aqua("-").yellow("hours").aqua(":").yellowR("minutes")
                    .add(" and all are ").yellowR("numbers").add(";").nl()
                .aquaR(" -> ").add("If you wish to specify an ending time, it must be done in a ")
                    .yellowR("24h format (0-23)").add(". If not, then just type ").yellowR("\"-\"")
                    .add(" instead to have a ").yellowR("permanent message").add(";").nl()
                .aquaR(" -> ").add("Everything added after the time is considered a message, so ")
                    .yellowR("it's irrelevant to use double quotes").add(";").nl()
                .aquaR(" -> ").add("You can use ").yellowR("{date}").add(" and ").yellowR("{time}")
                    .add(" and they will be replaced on the message by the end date you've specified.")
                    .add(" This is useful if you wish for that message to also be a notification until, for example,")
                    .add(" the start of an event.").nl()
                .nl()
                .yellowR("E.g.: ").nl()
                .add("/ces core server messages new ").aquaR("-").goldR(" Welcome to CraftEra!").nl().nl()
                .yellowR("E.g.: ").nl()
                .add("/ces core server messages new ").aquaR("25/12/2021-16:00").goldR(" Christmas on {date} at {time}!").nl().nl()
                .toString();
    }
}
