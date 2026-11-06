package io.github.pepe_pl.patryks_mod;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class ModItems {
    // Metoda rejestrująca:
    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    } // <- tutaj metoda register się kończy!

    // Nasz zarejestrowany przedmiot z własną klasą:
    public static final Item PATRYKS_SWORD = register(ModItemIds.PATRYKS_SWORD, PatryksSwordItem::new, new Item.Properties());

    // Metoda wymuszająca załadowanie klasy przez Javę:
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.PATRYKS_SWORD));
    }
}