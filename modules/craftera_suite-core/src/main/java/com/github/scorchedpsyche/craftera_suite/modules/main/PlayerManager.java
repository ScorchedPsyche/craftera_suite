package com.github.scorchedpsyche.craftera_suite.modules.main;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    public PlayerManager(Player player)
    {
        this.player = player;
    }

    public Player player;
    public SubtitleManager subtitle = new SubtitleManager();
    public TitleSubtitleManager titleSubtitleManager = new TitleSubtitleManager();
    public List<String> messages = new ArrayList<>();
}
