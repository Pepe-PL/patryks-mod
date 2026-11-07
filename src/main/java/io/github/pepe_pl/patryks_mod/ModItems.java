package io.github.pepe_pl.patryks_mod;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class ModItems {
    // Metoda rejestrująca:
    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    } // <- tutaj metoda register się kończy!

    // Nasz zarejestrowany miecz z własną klasą i właściwościami miecza:
    // Obrażenia: 241.0f + 8.0f (materiał) + 1.0f (gracz) = 250.0 HP (125 serduszek!)
    // Szybkość ataku: 100.0f (błyskawiczny atak pod autokliker)
    // Stały fioletowy połysk zaklęcia (Enchantment Glint Override)
    public static final Item PATRYKS_SWORD = register(
            ModItemIds.PATRYKS_SWORD,
            PatryksSwordItem::new,
            new Item.Properties()
                    .sword(ModToolMaterials.PATRYKS_TOOL_MATERIAL, 241.0f, 100.0f)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );

    // Zbroja Patryka – „super wytrzymała":
    // Trwałość ~2x netherit, obrona 4/9/7/4, twardość 5.0, odporność na odrzucenie 0.2.
    // Stały fioletowy połysk zaklęcia (Enchantment Glint Override) – jak miecz.
    public static final Item PATRYKS_HELMET = register(
            ModItemIds.PATRYKS_HELMET,
            Item::new,
            new Item.Properties()
                    .humanoidArmor(PatryksArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(PatryksArmorMaterial.BASE_DURABILITY))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );

    public static final Item PATRYKS_CHESTPLATE = register(
            ModItemIds.PATRYKS_CHESTPLATE,
            Item::new,
            new Item.Properties()
                    .humanoidArmor(PatryksArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(PatryksArmorMaterial.BASE_DURABILITY))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );

    public static final Item PATRYKS_LEGGINGS = register(
            ModItemIds.PATRYKS_LEGGINGS,
            Item::new,
            new Item.Properties()
                    .humanoidArmor(PatryksArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(PatryksArmorMaterial.BASE_DURABILITY))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );

    public static final Item PATRYKS_BOOTS = register(
            ModItemIds.PATRYKS_BOOTS,
            Item::new,
            new Item.Properties()
                    .humanoidArmor(PatryksArmorMaterial.INSTANCE, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(PatryksArmorMaterial.BASE_DURABILITY))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );

    // Klucz naszej własnej zakładki w trybie kreatywnym:
    public static final ResourceKey<CreativeModeTab> PATRYKS_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Main.id("patryks_tab")
    );

    // Nasza własna zakładka z ikoną miecza:
    public static final CreativeModeTab PATRYKS_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.PATRYKS_SWORD))
            .title(Component.translatable("creativeTab.patryks-mod"))
            .displayItems((_, output) -> {
                output.accept(ModItems.PATRYKS_SWORD);
                output.accept(ModItems.PATRYKS_HELMET);
                output.accept(ModItems.PATRYKS_CHESTPLATE);
                output.accept(ModItems.PATRYKS_LEGGINGS);
                output.accept(ModItems.PATRYKS_BOOTS);
            })
            .build();

    // Metoda wymuszająca załadowanie klasy przez Javę:
    public static void initialize() {
        // Rejestracja własnej zakładki:
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PATRYKS_TAB_KEY, PATRYKS_TAB);

        // Dodanie przedmiotów także do zakładki Combat (Walka):
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> {
                    creativeTab.accept(ModItems.PATRYKS_SWORD);
                    creativeTab.accept(ModItems.PATRYKS_HELMET);
                    creativeTab.accept(ModItems.PATRYKS_CHESTPLATE);
                    creativeTab.accept(ModItems.PATRYKS_LEGGINGS);
                    creativeTab.accept(ModItems.PATRYKS_BOOTS);
                });

        // TODO: Dodać pozostałe narzędzia Patryka (kilof, topór, łopata, motyka)
    }
}