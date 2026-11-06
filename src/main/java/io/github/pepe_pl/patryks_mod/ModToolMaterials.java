package io.github.pepe_pl.patryks_mod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ModToolMaterials {
    // Tag przedmiotów, które naprawiają ten materiał w kowadle:
    public static final TagKey<Item> REPAIRS_PATRYKS_SWORD = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Main.id("repairs_patryks_sword")
    );

    // Tag bloków nieodpowiednich do dropu:
    public static final TagKey<Block> INCORRECT_FOR_PATRYKS_TOOL = TagKey.create(
            Registries.BLOCK,
            Main.id("incorrect_for_patryks_tool")
    );

    // Definicja naszego potężnego materiału narzędzi:
    public static final ToolMaterial PATRYKS_TOOL_MATERIAL = new ToolMaterial(
            INCORRECT_FOR_PATRYKS_TOOL, // nasz dedykowany tag
            5000,                       // durability (trwałość)
            12.0F,                      // speed (prędkość kopania)
            8.0F,                       // attack damage bonus
            20,                         // enchantment value (zaklinanie)
            REPAIRS_PATRYKS_SWORD       // naprawa odłamkiem ametystu
    );
}
