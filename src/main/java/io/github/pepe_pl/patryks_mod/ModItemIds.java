package io.github.pepe_pl.patryks_mod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        return ResourceKey.create(Registries.ITEM, Main.id(name));
    }

    public static final ResourceKey<Item> PATRYKS_SWORD = create("patryks_sword");

    public static final ResourceKey<Item> PATRYKS_HELMET = create("patryks_helmet");
    public static final ResourceKey<Item> PATRYKS_CHESTPLATE = create("patryks_chestplate");
    public static final ResourceKey<Item> PATRYKS_LEGGINGS = create("patryks_leggings");
    public static final ResourceKey<Item> PATRYKS_BOOTS = create("patryks_boots");
}