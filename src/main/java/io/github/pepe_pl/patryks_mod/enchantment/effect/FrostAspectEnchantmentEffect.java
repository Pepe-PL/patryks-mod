package io.github.pepe_pl.patryks_mod.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.NullMarked;

import io.github.pepe_pl.patryks_mod.effect.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

@NullMarked
public record FrostAspectEnchantmentEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {
    public static final MapCodec<FrostAspectEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("duration").forGetter(FrostAspectEnchantmentEffect::duration)
            ).apply(instance, FrostAspectEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity mob) {
            MobEffectInstance slow = mob.getEffect(MobEffects.SLOWNESS);
            boolean frozen = slow != null && slow.getAmplifier() >= 4;

            // 1. Jeśli mob jest już zamrożony – wystrzelenie go jak pocisku (tryb "kopa"):
            if (frozen && context.owner() instanceof LivingEntity attacker) {
                Vec3 look = attacker.getLookAngle();
                Vec3 flat = new Vec3(look.x, 0, look.z);
                if (flat.lengthSqr() < 0.001) {
                    Vec3 toMob = mob.position().subtract(attacker.position());
                    flat = new Vec3(toMob.x, 0, toMob.z);
                }
                if (flat.lengthSqr() < 0.001) {
                    flat = new Vec3(0, 0, 1);
                }
                Vec3 dir = flat.normalize();

                mob.setDeltaMovement(dir.scale(1.3).add(0, 0.3, 0));
                mob.hurtMarked = true;
                mob.addEffect(new MobEffectInstance(ModMobEffects.FROST_FROZEN, 100, level, false, false, false));

                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, mob.getX(), mob.getY() + 0.8, mob.getZ(),
                        30, 0.3, 0.5, 0.3, 0.02);
                serverLevel.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                        SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 0.8f);
                return;
            }

            int durationTicks = Math.max(20, (int) this.duration.calculate(level));

            // 2. Zamrożenie w miejscu – ekstremalnie silne spowolnienie (V) + wyzerowanie ruchu:
            mob.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, durationTicks, 4, false, false, true));
            mob.addEffect(new MobEffectInstance(ModMobEffects.FROST_FROZEN, durationTicks, level, false, false, false));
            mob.setDeltaMovement(Vec3.ZERO);
            mob.hurtMarked = true;

            // 3. Białe i jasnoniebieskie cząsteczki mrozu wokół trafienia:
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, mob.getX(), mob.getY() + 0.8, mob.getZ(),
                    40, 0.4, 0.6, 0.4, 0.02);
            serverLevel.sendParticles(ParticleTypes.SPLASH, mob.getX(), mob.getY() + 0.8, mob.getZ(),
                    20, 0.35, 0.5, 0.35, 0.01);
            serverLevel.sendParticles(ParticleTypes.CLOUD, mob.getX(), mob.getY() + 0.8, mob.getZ(),
                    15, 0.3, 0.4, 0.3, 0.005);

            // 4. Dźwięk zamarzania:
            serverLevel.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                    SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 0.6f);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
