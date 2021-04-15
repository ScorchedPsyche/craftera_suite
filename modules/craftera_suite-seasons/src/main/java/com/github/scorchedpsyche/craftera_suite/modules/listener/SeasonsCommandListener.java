package com.github.scorchedpsyche.craftera_suite.modules.listener;

import com.github.scorchedpsyche.craftera_suite.modules.events.modules.seasons.SeasonsCommandEvent;
import com.github.scorchedpsyche.craftera_suite.modules.main.SeasonManager;
import com.github.scorchedpsyche.craftera_suite.modules.util.ConsoleUtil;
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
                case "create": // /ces seasons config
                    ConsoleUtil.logSuccess("ces seasons create");
                    break;

                case "current": // /ces seasons config
                    ConsoleUtil.logSuccess("ces seasons current");
                    break;

                case "end": // /ces seasons config
                    ConsoleUtil.logSuccess("ces seasons end");
                    break;

                case "manage": // /ces seasons config
                    ConsoleUtil.logSuccess("ces seasons manage");
                    break;

                case "start": // /ces seasons toggle
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
}

