package io.github.pepe_pl.patryks_mod.enchantment;

import com.mojang.serialization.MapCodec;

import io.github.pepe_pl.patryks_mod.Main;
import io.github.pepe_pl.patryks_mod.enchantment.effect.AmethystRetributionEnchantmentEffect;
import io.github.pepe_pl.patryks_mod.enchantment.effect.FrostAspectEnchantmentEffect;
import io.github.pepe_pl.patryks_mod.enchantment.effect.LightningEnchantmentEffect;
import io.github.pepe_pl.patryks_mod.enchantment.effect.SetBonusEnchantmentEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

@SuppressWarnings("unused")
public class ModEnchantmentEffects {
    public static final MapCodec<LightningEnchantmentEffect> LIGHTNING_EFFECT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            Main.id("lightning_effect"),
            LightningEnchantmentEffect.CODEC
    );

    public static final MapCodec<AmethystRetributionEnchantmentEffect> AMETHYST_RETRIBUTION_EFFECT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            Main.id("amethyst_retribution_effect"),
            AmethystRetributionEnchantmentEffect.CODEC
    );

    public static final MapCodec<FrostAspectEnchantmentEffect> FROST_ASPECT_EFFECT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            Main.id("frost_aspect_effect"),
            FrostAspectEnchantmentEffect.CODEC
    );

    public static final MapCodec<SetBonusEnchantmentEffect> SET_BONUS_EFFECT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            Main.id("set_bonus_effect"),
            SetBonusEnchantmentEffect.CODEC
    );

    public static void initialize() {
        // Sama inicjalizacja klasy wymusza rejestrację statycznych pól w registry.
    }
}
