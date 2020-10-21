package com.github.scorchedpsyche.craftera_suite.modules.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtils
{
    @Nullable
    public Integer getItemRemainingDurability(ItemStack item)
    {
        if (item != null && item.getType().getMaxDurability() != 0)
        {
            return item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
        }

        return null;
    }
}
