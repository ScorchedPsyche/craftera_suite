package com.github.scorchedpsyche.craftera_suite.modules.task;

import com.github.scorchedpsyche.craftera_suite.modules.model.RunnableModel;
import com.github.scorchedpsyche.craftera_suite.modules.util.PlayerHeadUtil;

public class PreloadPlayerHeadsTask extends RunnableModel {
    public PreloadPlayerHeadsTask(String prefix, String name) {
        super(prefix, name);
    }

    @Override
    public void run() {
        super.setAsRunning();
        PlayerHeadUtil.preloadPlayerHeads();
        super.cancel();
    }
}
