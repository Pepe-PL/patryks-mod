package io.github.pepe_pl.patryks_mod.enchantment;

import com.mojang.serialization.MapCodec;

import io.github.pepe_pl.patryks_mod.Main;
import io.github.pepe_pl.patryks_mod.enchantment.effect.LightningEnchantmentEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModEnchantmentEffects {
    public static final MapCodec<LightningEnchantmentEffect> LIGHTNING_EFFECT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            Main.id("lightning_effect"),
            LightningEnchantmentEffect.CODEC
    );

    public static void initialize() {
        // Dotknięcie pola wymusza załadowanie klasy i rejestrację efektu w registry.
        if (LIGHTNING_EFFECT == null) {
            throw new IllegalStateException("Failed to register lightning_effect");
        }
    }
}
