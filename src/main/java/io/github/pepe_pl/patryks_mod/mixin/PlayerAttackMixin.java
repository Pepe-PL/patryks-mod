package io.github.pepe_pl.patryks_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.github.pepe_pl.patryks_mod.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(Player.class)
public abstract class PlayerAttackMixin {
    private static final ResourceKey<Enchantment> FROST_ASPECT = ResourceKey.create(
            Registries.ENCHANTMENT, Main.id("frost_aspect"));

    @ModifyArg(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
            index = 1
    )
    private float patryksMod$zeroDamageWithFrostAspect(float damage) {
        Player self = (Player) (Object) this;
        ItemStack weapon = self.getMainHandItem();
        if (weapon.isEmpty()) {
            return damage;
        }

        return self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(FROST_ASPECT)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, weapon) > 0 ? 0.0f : damage)
                .orElse(damage);
    }
}
