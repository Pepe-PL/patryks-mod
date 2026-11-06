package io.github.pepe_pl.patryks_mod;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class PatryksSwordItem extends Item {
    public PatryksSwordItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        // Reset klatek nietykalności celu po uderzeniu - autokliker może zadawać pełne obrażenia bez opóźnień:
        target.invulnerableTime = 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.1").withStyle(ChatFormatting.GOLD));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.2").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.3").withStyle(ChatFormatting.GRAY));
    }
}
