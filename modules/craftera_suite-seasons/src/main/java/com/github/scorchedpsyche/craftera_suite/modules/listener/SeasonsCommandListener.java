package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.DateUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.GameUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerUtil;
import com.github.scorchedpsyche.craftera_suite.modules.util.natives.StringUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
            switch (args[0].toLowerCase())
            {
                case "create": // /ces seasons create
                    seasonManager.createSeason(
                            seasonManager.getNextAvailableSeasonNumber(),
                            "Untitled Season",
                            "",
                            SuitePluginManager.Seasons.Status.Inactive,
                            true,
                            DateUtil.Time.getUnixNow(),
                            0,
                            GameUtil.Version.getCurrent(),
                            "-");
                    break;

                case "current": // /ces seasons current
                    ConsoleUtil.logSuccess("ces seasons current");
                    break;

                case "end": // /ces seasons end
                    ConsoleUtil.logSuccess("ces seasons end");
                    break;

                case "manage": // /ces seasons manage
                    ConsoleUtil.logSuccess("ces seasons manage");
                    break;

                case "start": // /ces seasons start
                    ConsoleUtil.logSuccess("ces seasons start");
                    break;

                default: // /ces seasons HELP
                    ConsoleUtil.logSuccess("ces seasons help");
                    break;
            }
        } else
        {
            // Code shouldn't have gotten here as the CustomCommandExecutor sends at least "toggle". Log the error
            ConsoleUtil.logError("onSeasonsCommandEvent received null or empty args. Report this to the developer");
        }
    }

    private String getUsageMessage()
    {
        return "All arguments are optional and the command will create the next available season number. Command usage:\n\n" +

                "/ces seasons create 1 \"Season Title\" \"Season Name\"\n\n" +

                "Argument 1 = season number\n" +
                "Argument 2 = season title" +
                "Argument 3 = season subtitle";
    }
}

