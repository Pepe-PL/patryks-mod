package io.github.pepe_pl.patryks_mod;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

@NullMarked
@SuppressWarnings("deprecation")
public class PatryksShieldItem extends ShieldItem {
    public PatryksShieldItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_shield.1").withStyle(ChatFormatting.GOLD));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_shield.2").withStyle(ChatFormatting.GRAY));
    }
}
