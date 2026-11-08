package io.github.pepe_pl.patryks_mod;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@NullMarked
@SuppressWarnings("deprecation")
public class PatryksArmorItem extends Item {
    private static final ResourceKey<Enchantment> FEATHER_FALLING = ResourceKey.create(
            Registries.ENCHANTMENT, net.minecraft.resources.Identifier.withDefaultNamespace("feather_falling"));
    private static final ResourceKey<Enchantment> FIRE_PROTECTION = ResourceKey.create(
            Registries.ENCHANTMENT, net.minecraft.resources.Identifier.withDefaultNamespace("fire_protection"));
    private static final ResourceKey<Enchantment> FROST_RESISTANCE = ResourceKey.create(
            Registries.ENCHANTMENT, Main.id("frost_resistance"));

    private final @Nullable String tooltipKey;

    public PatryksArmorItem(Item.Properties properties, @Nullable String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        // Każda część zbroi Patryka ma Frost Resistance IV
        // (także z creative tab, /give i stare egzemplarze):
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var fr = registry.get(FROST_RESISTANCE);
        if (fr.isEmpty()) {
            return;
        }

        boolean boots = "patryks_boots".equals(tooltipKey);
        var ff = registry.get(FEATHER_FALLING);
        var fp = registry.get(FIRE_PROTECTION);

        EnchantmentHelper.updateEnchantments(stack, mutable -> {
            mutable.set(fr.get(), 4);
            // Buty Patryka mają też Feather Falling IV + Fire Protection IV:
            if (boots) {
                ff.ifPresent(holder -> mutable.set(holder, 4));
                fp.ifPresent(holder -> mutable.set(holder, 4));
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        if (tooltipKey != null) {
            textConsumer.accept(Component.translatable("itemTooltip.patryks-mod." + tooltipKey + ".1").withStyle(ChatFormatting.GOLD));
            textConsumer.accept(Component.translatable("itemTooltip.patryks-mod." + tooltipKey + ".2").withStyle(ChatFormatting.GRAY));
        }
    }
}
