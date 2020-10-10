package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.ChatColor;

public class StringUtilsHud
{
    public static final String playerCoordinatesExtended = "XYZ: ";
    public static final String playerCoordinatesExtendedColorized = ChatColor.GOLD + "XYZ: " + ChatColor.RESET;
    public static final String playerCoordinatesCompact = "x y z";
    public static final String playerCoordinatesCompactColorized =
            ChatColor.GOLD + "x " + ChatColor.RESET +   // insert at 0
            ChatColor.GOLD + "y " + ChatColor.RESET +   // insert at 6
            ChatColor.GOLD + "z" + ChatColor.RESET;     // insert at 12

    public static final String netherPortalCoordinatesExtended = "Portal XZ: ";
    public static final String netherPortalCoordinatesExtendedColorized =
            ChatColor.DARK_RED + "Portal XZ: " + ChatColor.RESET;
    public static final String netherPortalCoordinatesCompact = "x z";
    public static final String netherPortalCoordinatesCompactColorized =
            ChatColor.DARK_RED + "x " + ChatColor.RESET +   // insert at 0
            ChatColor.DARK_RED + "z" + ChatColor.RESET;          // insert at 6
}
