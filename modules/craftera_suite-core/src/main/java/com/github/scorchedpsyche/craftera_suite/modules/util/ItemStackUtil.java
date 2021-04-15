package com.github.scorchedpsyche.craftera_suite.modules.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtil
{
    @Nullable
    public static Integer getItemRemainingDurability(ItemStack item)
    {
        if (item != null && item.getType().getMaxDurability() != 0)
        {
            return item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
        }

        return null;
    }
}
