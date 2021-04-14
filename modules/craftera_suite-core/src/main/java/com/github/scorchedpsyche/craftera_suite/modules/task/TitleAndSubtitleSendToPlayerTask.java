package com.github.scorchedpsyche.craftera_suite.modules.task;

import com.github.scorchedpsyche.craftera_suite.modules.CraftEraSuiteCore;
import com.github.scorchedpsyche.craftera_suite.modules.model.RunnableModel;

public class TitleAndSubtitleSendToPlayerTask extends RunnableModel {
    public TitleAndSubtitleSendToPlayerTask(String prefix, String name) {
        super(prefix, name);
    }

    @Override
    public void run() {
        super.setAsRunning();
        CraftEraSuiteCore.sendTitleAndSubtitleToPlayers();
    }
}
