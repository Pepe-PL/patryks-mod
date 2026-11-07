package io.github.pepe_pl.patryks_mod;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

@NullMarked
@SuppressWarnings("deprecation")
public class PatryksArmorItem extends Item {
    private final @Nullable String tooltipKey;

    public PatryksArmorItem(Item.Properties properties, @Nullable String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        if (tooltipKey != null) {
            textConsumer.accept(Component.translatable("itemTooltip.patryks-mod." + tooltipKey + ".1").withStyle(ChatFormatting.GOLD));
            textConsumer.accept(Component.translatable("itemTooltip.patryks-mod." + tooltipKey + ".2").withStyle(ChatFormatting.GRAY));
        }
    }
}
