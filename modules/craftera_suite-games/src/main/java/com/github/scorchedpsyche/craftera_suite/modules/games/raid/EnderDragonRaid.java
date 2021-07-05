package com.github.scorchedpsyche.craftera_suite.modules.games.raid;

import com.github.scorchedpsyche.craftera_suite.modules.main.SuitePluginManager;
import com.github.scorchedpsyche.craftera_suite.modules.model.GameModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.StringFormattedModel;
import com.github.scorchedpsyche.craftera_suite.modules.model.TitleSubtitleModel;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EnderDragonRaid extends GameModel {
    public EnderDragonRaid(Player owner, SuitePluginManager.Games.Type type) {
        super(owner, type, "Ender Dragon Raid", Sound.ENTITY_ENDER_DRAGON_GROWL);
    }

    @Override
    public void start() {
        stage = SuitePluginManager.Games.Stage.Running;

        TitleSubtitleModel titleSubtitle = new TitleSubtitleModel(10, 100, 10);
        titleSubtitle.title.bold().aqua("Ready!");
        titleSubtitle.subtitle.yellowR("You can summon the Dragon!");

        sendTitleAndSubtitleToParticipants(titleSubtitle);
        sendMessageToParticipants(new StringFormattedModel().aquaR(getName()).add(" is now ready! ")
            .yellow("Use the End Crystals to summon the dragon!!").toString());
    }
}
