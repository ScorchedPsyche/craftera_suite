package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.SeasonModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.*;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class SeasonsCommandListener implements Listener
{
    public SeasonsCommandListener(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    SeasonManager seasonManager;

    @EventHandler
    public void onSeasonsCommandEvent(SeasonsCommandEvent event)
    {
        String[] args = event.getArgs();

        // Check if any arguments exists. If this check fails something went very wrong
        if (args != null && args.length > 0)
        {
            SeasonModel season;

            switch (args[0].toLowerCase()) {
                case "create_new_and_select_for_editing": // /ces seasons create_new_and_select_for_editing
                    season = seasonManager.createSeason(
                            seasonManager.getNextAvailableSeasonNumber(),
                            "Untitled Season",
                            "",
                            SuitePluginManager.Seasons.Status.Inactive.ordinal(),
                            true,
                            0,
                            0,
                            "-",
                            "-");

                    if (season == null) {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                ChatColor.RED + "Failed to create season.");
                    } else {
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append("Created season ");
                        strBuilder.append(ChatColor.GREEN);
                        strBuilder.append(ChatColor.BOLD);
                        strBuilder.append(season.getNumber());
                        strBuilder.append(" - ");
                        strBuilder.append(season.getTitle());
                        strBuilder.append(ChatColor.RESET);
                        strBuilder.append(" and selected it for editing.");
                        strBuilder.append(MessageUtil.newLine());
                        strBuilder.append(MessageUtil.newLine());
                        strBuilder.append("Use \"");
                        strBuilder.append(ChatColor.GOLD);
                        strBuilder.append(ChatColor.BOLD);
                        strBuilder.append(MessageUtil.formattedCommand("/ces seasons edit_selected ..."));
                        strBuilder.append(ChatColor.RESET);
                        strBuilder.append("\" to edit it's properties.");

                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                strBuilder.toString());

                        seasonManager.selected = season;
                    }
                    break;

                case "current": // /ces seasons current
                    season = seasonManager.fetchActiveSeason();

                    if (season == null) {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                "No active season was found.");
                    } else {
                        StringBuilder strBuilder = new StringBuilder("Season " + season.getNumber());

                        // Title
                        if (!StringUtil.isNullOrEmpty(season.getTitle())) {
                            strBuilder.append(": " + season.getTitle());
                        }

                        // Subtitle
                        if (!StringUtil.isNullOrEmpty(season.getSubtitle())) {
                            strBuilder.append(" [" + season.getSubtitle() + "]");
                        }

                        // Recording stats
                        if (season.getAccount() == 1) {
                            strBuilder.append(ChatColor.GREEN + "\n Recording stats! (playtime, achievements, etc");
                        } else {
                            strBuilder.append(ChatColor.RED + "\n NOT recording stats. (playtime, achievements, etc");
                        }

                        // Date & Version stats
                        if (season.getDate_start() == 0) {
                            strBuilder.append(ChatColor.YELLOW + "\n Not yet started");
                        } else {
                            // TODO: display stats if season has started
                        }

                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                strBuilder.toString());
                    }
                    break;

                case "list": // /ces seasons list
                    if (args.length > 1) {
                        try {
                            int num = Integer.parseInt(args[1]);
                            List<SeasonModel> seasons = seasonManager.fetchListingByPage(num);

                            // Check if any season was found
                            if ( seasons != null && !seasons.isEmpty() ) {
                                // Seasons found. List them
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                        "======== Page Number " + num + " ========");

                                StringBuilder msg;
                                for( SeasonModel season_listing : seasons )
                                {
                                    msg = new StringBuilder("Season ");
                                    msg.append(ChatColor.YELLOW);
                                    msg.append(season_listing.getNumber());
                                    msg.append(ChatColor.RESET);
                                    if( !StringUtil.isNullOrEmpty(season_listing.getTitle()) )
                                    {
                                        msg = new StringBuilder(" - ");
                                        msg.append(season_listing.getTitle());
                                    }
                                    if( !StringUtil.isNullOrEmpty(season_listing.getSubtitle()) )
                                    {
                                        msg = new StringBuilder(" (");
                                        msg.append(season_listing.getSubtitle());
                                        msg = new StringBuilder(" )");
                                    }

                                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                            msg.toString());
                                }
                            } else {
                                // No seasons were found
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                        "No seasons were found.");
                            }
                        } catch (NumberFormatException e) {
                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                    ChatColor.RED + "Page must be a number. E.g.: "
                                            + MessageUtil.formattedCommand("/ces seasons list 10"));
                        }
                    } else {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, seasonsCommandHelpMessage());
                    }
                    break;

                case "select": // /ces seasons select
                    if (args.length > 1) {
                        try {
                            int num = Integer.parseInt(args[1]);
                            season = seasonManager.fetchSeason(num);

                            // Check if a season was found
                            if (season == null) {
                                // Not found. Display error
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                        "Season " + ChatColor.YELLOW + num + " - " + season.getTitle() + ChatColor.RESET
                                                + " was " + ChatColor.RED + "not found!");
                            } else {
                                // Found. Mark it as selected
                                seasonManager.selected = season;
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                        "Season " + ChatColor.YELLOW + num + " - " + season.getTitle() + ChatColor.RESET
                                                + " was " + ChatColor.GREEN + "selected for editing!");
                            }
                        } catch (NumberFormatException e) {
                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                    ChatColor.RED + "Season must be a number. E.g.: "
                                            + MessageUtil.formattedCommand("/ces seasons select_for_editing 10"));
                        }
                    } else {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, seasonsCommandHelpMessage());
                    }
                    break;

                case "selected": // /ces seasons selected
                    if (args.length > 1) {
                        switch (args[1].toLowerCase()) {
                            case "delete": // /ces seasons selected delete
                                break;

                            case "display": // /ces seasons selected display
                                if (seasonManager.selected == null) {
                                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                            "No season is currently selected for editing.");
                                } else {
                                    StringBuilder strBuilder = new StringBuilder("Season " + seasonManager.selected.getNumber());

                                    // Title
                                    if (!StringUtil.isNullOrEmpty(seasonManager.selected.getTitle())) {
                                        strBuilder.append(": " + seasonManager.selected.getTitle());
                                    }

                                    // Subtitle
                                    if (!StringUtil.isNullOrEmpty(seasonManager.selected.getSubtitle())) {
                                        strBuilder.append(" [" + seasonManager.selected.getSubtitle() + "]");
                                    }

                                    // Recording stats
                                    if (seasonManager.selected.getAccount() == 1) {
                                        strBuilder.append(ChatColor.GREEN + "\n Recording stats! (achievements, playtime, etc)");
                                    } else {
                                        strBuilder.append(ChatColor.RED + "\n NOT recording stats. (achievements, playtime, etc)");
                                    }

                                    // Date & Version stats
                                    if (seasonManager.selected.getDate_start() == 0) {
                                        strBuilder.append(ChatColor.YELLOW + "\n Not yet started");
                                    } else {
                                        strBuilder.append("Started on " + ChatColor.YELLOW
                                                + DateUtil.Time.unixToDate(seasonManager.selected.getDate_start()));
                                    }

                                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                            strBuilder.toString());
                                }
                                break;

                            case "edit": // /ces seasons selected edit
                                if (args.length > 2) {
                                    switch (args[2].toLowerCase()) {
                                        case "account": // /ces seasons selected edit account
                                            if (args.length > 3) {
                                                switch (args[3].toLowerCase()) {
                                                    case "enable": // /ces seasons selected edit account enable
                                                        if (!seasonManager.isSelectedSeasonValid()) {
                                                            sendInvalidSelectedSeasonMessageToPlayer(event.getPlayer());
                                                            break;
                                                        }
                                                        seasonManager.selected.setAccount(1);
                                                        if (seasonManager.updateSeason(seasonManager.selected)) {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "Season " + ChatColor.YELLOW + seasonManager.selected.getNumber() + ChatColor.RESET
                                                                            + " accounting was " + ChatColor.GREEN + "enabled " + ChatColor.RESET
                                                                            + " (will collect player activity data when season is "
                                                                            + ChatColor.YELLOW + "ACTIVE" + ChatColor.RESET + ").");
                                                        } else {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    ChatColor.RED + "Failed to enable season accounting. Contact server admin!");
                                                        }
                                                        break;

                                                    case "disable": // /ces seasons selected edit account enable
                                                        if (!seasonManager.isSelectedSeasonValid()) {
                                                            sendInvalidSelectedSeasonMessageToPlayer(event.getPlayer());
                                                            break;
                                                        }
                                                        seasonManager.selected.setAccount(0);
                                                        if (seasonManager.updateSeason(seasonManager.selected)) {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "Season " + ChatColor.YELLOW + seasonManager.selected.getNumber() + ChatColor.RESET
                                                                            + " accounting was " + ChatColor.RED + "disabled " + ChatColor.RESET
                                                                            + " (won't collect player activity data when season is "
                                                                            + ChatColor.YELLOW + "ACTIVE" + ChatColor.RESET + ").");
                                                        } else {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    ChatColor.RED + "Failed to disabled season accounting. Contact server admin!");
                                                        }
                                                        break;

                                                    default:
                                                        break;
                                                }
                                            } else
                                            {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, accountCommandHelpMessage());
                                            }
                                            break;

                                        case "number": // /ces seasons selected edit number
                                            break;

                                        case "status": // /ces seasons selected edit status
                                            if (args.length > 3) {
                                                switch (args[3].toLowerCase()) {
                                                    case "activate": // /ces seasons selected edit status activate
                                                        break;

                                                    case "deactivate": // /ces seasons selected edit status deactivate
                                                        break;

                                                    case "start": // /ces seasons selected edit status start
                                                        if (seasonManager.selected == null) {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "No season is currently selected for editing.");
                                                        } else {
                                                            // Check if season is active (can't start if not)
                                                            if (seasonManager.selected.getStatus() == SuitePluginManager.Seasons.Status.Active.ordinal()) {
                                                                seasonManager.selected.setStatus(SuitePluginManager.Seasons.Status.Started.ordinal());
                                                                seasonManager.selected.setDate_start(DateUtil.Time.getUnixNow());
                                                                seasonManager.selected.setMinecraft_version_start(GameUtil.Version.getCurrent());
                                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                        ChatColor.GREEN + "Season " + seasonManager.selected.getNumber() + " STARTED!");
                                                            } else {
                                                                // Season not active
                                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                        ChatColor.RED + "Cannot start a season that's not in "
                                                                                + ChatColor.YELLOW + SuitePluginManager.Seasons.Status.Active.toString()
                                                                                + ChatColor.RESET + " state! Maybe it's already running?");
                                                            }
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "Season must be a number. E.g.: "
                                                                            + MessageUtil.formattedCommand("/ces seasons select_for_editing 10"));
                                                        }
                                                        break;

                                                    case "end": // /ces seasons selected edit status end
                                                        if (seasonManager.selected == null) {
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "No season is currently selected for editing.");
                                                        } else {
                                                            // Check if season is started (can't end if not)
                                                            if (seasonManager.selected.getStatus() == SuitePluginManager.Seasons.Status.Started.ordinal()) {
                                                                seasonManager.selected.setStatus(SuitePluginManager.Seasons.Status.Finished.ordinal());
                                                                seasonManager.selected.setDate_end(DateUtil.Time.getUnixNow());
                                                                seasonManager.selected.setMinecraft_version_end(GameUtil.Version.getCurrent());
                                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                        ChatColor.GREEN + "Season " + seasonManager.selected.getNumber() + " ENDED!");
                                                            } else {
                                                                // Season not active
                                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                        ChatColor.RED + "Cannot start a season that's not in "
                                                                                + ChatColor.YELLOW + SuitePluginManager.Seasons.Status.Active.toString()
                                                                                + ChatColor.RESET + " state! Maybe it's already running?");
                                                            }
                                                            PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                                    "Season must be a number. E.g.: "
                                                                            + MessageUtil.formattedCommand("/ces seasons select_for_editing 10"));
                                                        }
                                                        break;

                                                    case "archive": // /ces seasons selected edit status archive
                                                        break;

                                                    default: // /ces seasons selected edit status
                                                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, statusCommandHelpMessage());
                                                        break;
                                                }
                                            } else
                                            {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, statusCommandHelpMessage());
                                            }
                                            break;

                                        case "subtitle": // /ces seasons selected edit subtitle
                                            if (!seasonManager.isSelectedSeasonValid()) {
                                                sendInvalidSelectedSeasonMessageToPlayer(event.getPlayer());
                                                break;
                                            }

                                            seasonManager.selected.setSubtitle(
                                                    CommandUtil.captureStringsStartingFromArgPositionAndRemoveDoubleQuotes(args, 2)
                                            );
                                            if (seasonManager.updateSeason(seasonManager.selected)) {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                        "Season " + ChatColor.YELLOW + seasonManager.selected.getNumber() + ChatColor.RESET
                                                                + "'s subtitle was changed to "
                                                                + ChatColor.GREEN + seasonManager.selected.getSubtitle() + ChatColor.RESET + ".");
                                            } else {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                        ChatColor.RED + "Failed to change season's subtitle. Contact server admin!");
                                            }
                                            break;

                                        case "title": // /ces seasons selected edit title
                                            if (!seasonManager.isSelectedSeasonValid()) {
                                                sendInvalidSelectedSeasonMessageToPlayer(event.getPlayer());
                                                break;
                                            }

                                            seasonManager.selected.setTitle(
                                                    CommandUtil.captureStringsStartingFromArgPositionAndRemoveDoubleQuotes(args, 2)
                                            );
                                            if (seasonManager.updateSeason(seasonManager.selected)) {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                        "Season " + ChatColor.YELLOW + seasonManager.selected.getNumber() + ChatColor.RESET
                                                                + "'s title was changed to "
                                                                + ChatColor.GREEN + seasonManager.selected.getTitle() + ChatColor.RESET + ".");
                                            } else {
                                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact,
                                                        ChatColor.RED + "Failed to change season's title. Contact server admin!");
                                            }
                                            break;

                                        default:
                                            break;
                                    }
                                } else
                                {
                                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, editCommandHelpMessage());
                                }
                                break;

                            default: // /ces seasons selected
                                PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, selectedCommandHelpMessage());
                                break;
                        }
                    } else
                    {
                        PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, selectedCommandHelpMessage());
                    }
                    break;

                default: // /ces seasons
                    PlayerUtil.sendMessageWithPluginPrefix(event.getPlayer(), SuitePluginManager.Seasons.Name.compact, seasonsCommandHelpMessage());
                    break;
            }
        } else
        {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtil.logError("onSeasonsCommandEvent received null or empty args. Report this to the developer");
        }
    }

    private void sendInvalidSelectedSeasonMessageToPlayer(Player player)
    {
        PlayerUtil.sendMessageWithPluginPrefix(player, SuitePluginManager.Seasons.Name.compact,
                ChatColor.RED + "There is not valid selected season. Use "+ MessageUtil.formattedCommand("/ces seasons select_for_editing X"));
    }

    /**
     * '/ces seasons' help command full page.
     * @return Formatted string with full Seasons help instructions
     */
    private String seasonsCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces seasons ...") + ":" +
                MessageUtil.newLine() +
                MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... create_new_and_select_for_editing",
                        "create a new default season with the next season number available and selects it for editing;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... current",
                        "displays info about the current season (either active or running);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... list X",
                        "list seasons where " + ChatColor.GOLD + "X" + ChatColor.RESET + " is the page number;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... select X",
                        "selects season " + ChatColor.GOLD + "X" + ChatColor.RESET + " for editing;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... selected ...",
                        "command for managing the currently selected season (has subcommands).");
    }

    /**
     * '/ces seasons selected' help command full page.
     * @return Formatted string with full Seasons help instructions
     */
    private String selectedCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces seasons selected ...") + ":" +
                MessageUtil.newLine() +
                MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... delete",
                        "deletes the selected season;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... display",
                        "displays info about the selected season;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... edit ...",
                        "edit properties for the selected season (has subcommands).");
    }

    /**
     * '/ces seasons selected edit' command full help page.
     * @return Formatted string with full Seasons help instructions
     */
    private String editCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces seasons selected edit ...") + ":" +
                MessageUtil.newLine() +
                MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... account ...",
                        "configures data gathering for the currently selected season;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... number X",
                        "sets a new number (X) for the season;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... status ...",
                        "configures the state of the current season (Archives, Active, Started, etc);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... subtitle \"New Subtitle\"",
                        "sets a new subtitle for the season;\n") +
            MessageUtil.formattedCommandWithDescription(
                    "... title \"New Title\"",
                    "sets a new title for the season.");
    }

    /**
     * '/ces seasons selected edit account' command full help page.
     * @return Formatted string with full Seasons help instructions
     */
    private String accountCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces seasons selected edit account ...") + ":" +
                MessageUtil.newLine() +
                MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... enable",
                        "enables data recording for this season (achievements, playtime, etc.);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... disable",
                        "disables data recording.");
    }

    /**
     * '/ces seasons selected edit status' command full help page.
     * @return Formatted string with full Seasons help instructions
     */
    private String statusCommandHelpMessage()
    {
        return  "\nSubcommands for " + MessageUtil.formattedCommand("/ces seasons selected edit status ...") + ":" +
                MessageUtil.newLine() +
                MessageUtil.newLine() +

                MessageUtil.formattedCommandWithDescription(
                        "...",
                        "displays this page;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... activate",
                        "sets the currently selected season as active (enabled it to gather data if account settings is enabled);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... archive",
                        "archives a finished season;\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... deactivate",
                        "deactivates the currently selected season (disabled data gathering);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... finish",
                        "finishes a started season (10 second countdown for all players);\n") +
                MessageUtil.formattedCommandWithDescription(
                        "... start",
                        "starts an active season (10 second countdown for all players).");
    }
}

