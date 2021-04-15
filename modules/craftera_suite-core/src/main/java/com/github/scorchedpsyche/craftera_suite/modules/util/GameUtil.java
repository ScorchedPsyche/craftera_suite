package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.Bukkit;

public class GameUtil {
    public static class Version {
        public static String getCurrent()
        {
            return  Bukkit.getBukkitVersion().split("-")[0];
        }
    }

}
