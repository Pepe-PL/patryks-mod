package io.github.pepe_pl.patryks_mod.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.NullMarked;

import io.github.pepe_pl.patryks_mod.HitParticleRings;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

@NullMarked
public record AmethystRetributionEnchantmentEffect(LevelBasedValue damage) implements EnchantmentEntityEffect {
    public static final MapCodec<AmethystRetributionEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("damage").forGetter(AmethystRetributionEnchantmentEffect::damage)
            ).apply(instance, AmethystRetributionEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        // Aktywuje się gdy noszący zbroję (victim) zostaje zaatakowany – target to atakujący.
        if (target instanceof LivingEntity attacker && context.owner() instanceof LivingEntity wearer) {
            float dmg = this.damage.calculate(level);

            // 1. Fioletowe cząsteczki ametystu na atakującym:
            serverLevel.sendParticles(ParticleTypes.PORTAL, attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                    30, 0.4, 0.6, 0.4, 0.05);
            serverLevel.sendParticles(ParticleTypes.WITCH, attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                    15, 0.3, 0.5, 0.3, 0.02);
            HitParticleRings.spawn(serverLevel, attacker.getX(), attacker.getY() + 0.2, attacker.getZ());

            // 2. Obrażenia odwetowe (typ thorns, jak kolce):
            attacker.invulnerableTime = 0;
            attacker.hurtServer(serverLevel, serverLevel.damageSources().thorns(wearer), dmg);

            // 3. Dźwięk rezonansu ametystu:
            serverLevel.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5f, 1.4f);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
