package io.github.pepe_pl.patryks_mod;

import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class PatryksArmorMaterial {
    // Bazowa trwałość materiału – ArmorType mnoży ją per slot (hełm ×13, napierśnik ×16, nogawice ×15, buty ×13).
    // 60 daje ~2x większą trwałość niż netherit (base 37).
    public static final int BASE_DURABILITY = 60;

    // Klucz assetu tekstur zbroi na modelu gracza:
    // assets/patryks-mod/textures/entity/equipment/humanoid/patryks.png
    // assets/patryks-mod/textures/entity/equipment/humanoid_leggings/patryks.png
    // assets/patryks-mod/textures/entity/equipment/humanoid_baby/patryks.png
    public static final ResourceKey<EquipmentAsset> PATRYKS_ARMOR_MATERIAL_KEY = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Main.id("patryks")
    );

    // Tag przedmiotów naprawiających zbroję w kowadle (odłamki ametystu, jak miecz):
    public static final TagKey<Item> REPAIRS_PATRYKS_ARMOR = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Main.id("repairs_patryks_armor")
    );

    // Materiał zbroi Patryka – znacznie trwalszy i twardszy od netheritu:
    // Obrona: hełm 4 / napierśnik 9 / nogawice 7 / buty 4 (netherit: 3/8/6/3)
    // Twardość 5.0 (netherit: 3.0), odporność na odrzucenie 0.2 (netherit: 0.1)
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 4,
                    ArmorType.CHESTPLATE, 9,
                    ArmorType.LEGGINGS, 7,
                    ArmorType.BOOTS, 4
            ),
            20,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            5.0F,
            0.2F,
            REPAIRS_PATRYKS_ARMOR,
            PATRYKS_ARMOR_MATERIAL_KEY
    );
}
