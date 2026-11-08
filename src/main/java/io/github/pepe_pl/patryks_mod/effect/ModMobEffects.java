package io.github.pepe_pl.patryks_mod.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

import io.github.pepe_pl.patryks_mod.Main;

public class ModMobEffects {
    public static final Holder<MobEffect> FROST_FROZEN = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            Main.id("frost_frozen"),
            new FrostFrozenMobEffect()
    );

    public static void initialize() {
        // Sama inicjalizacja klasy wymusza rejestrację statycznych pól w registry.
    }
}
