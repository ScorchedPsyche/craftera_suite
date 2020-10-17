package com.github.scorchedpsyche.craftera_suite.modules.interfaces;

import org.bukkit.entity.Player;

public interface ICommandEvent
{
    Player getPlayer();
    String[] getArgs();
}
