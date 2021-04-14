package com.github.scorchedpsyche.craftera_suite.modules.task;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.RunnableModel;
import com.github.scorchedpsyche.craftera_suite.modules.utils.ConsoleUtils;
import com.github.scorchedpsyche.craftera_suite.modules.utils.PlayerHeadUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class PreloadPlayerHeadsTask extends RunnableModel {
    public PreloadPlayerHeadsTask(String prefix, String name) {
        super(prefix, name);
    }

    @Override
    public void run() {
        super.setAsRunning();
        PlayerHeadUtils.preloadPlayerHeads();
        super.cancel();
//        super.cancelWithOptionalLogMessage();
//        cancelWithPrefixedLogMessage();
    }

//    public void cancelWithPrefixedLogMessage()
//    {
//        ConsoleUtils.logMessage(SuitePluginManager.WanderingTrades.Name.full,
//                "TASK: PreloadPlayerHeads cancelled");
//        super.cancel();
//    }
}
