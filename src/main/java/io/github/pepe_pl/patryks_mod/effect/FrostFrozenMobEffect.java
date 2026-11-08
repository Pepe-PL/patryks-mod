package io.github.pepe_pl.patryks_mod.effect;

import org.jspecify.annotations.NullMarked;

import io.github.pepe_pl.patryks_mod.Main;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

@NullMarked
public class FrostFrozenMobEffect extends MobEffect {
    private static final ResourceKey<Enchantment> FROST_RESISTANCE = ResourceKey.create(
            Registries.ENCHANTMENT, Main.id("frost_resistance"));
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public FrostFrozenMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x99CCFF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        // 1. Ciągłe białe cząsteczki mrozu wokół zamrożonego moba co tick:
        level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY() + 0.8, entity.getZ(),
                6, 0.4, 0.7, 0.4, 0.02);
        level.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 0.8, entity.getZ(),
                3, 0.35, 0.6, 0.35, 0.005);
        level.sendParticles(ParticleTypes.SPLASH, entity.getX(), entity.getY() + 0.8, entity.getZ(),
                2, 0.35, 0.5, 0.35, 0.01);

        // 2. Jeśli zamrożony mob się porusza (np. wystrzelony lub popchnięty pistonem)
        //    i dotknie czegokolwiek – zadaje mu ogromne obrażenia:
        if (entity.getDeltaMovement().horizontalDistanceSqr() < 0.005) {
            return true;
        }

        var victims = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(0.6),
                victim -> victim.isAlive() && victim != entity);
        if (victims.isEmpty()) {
            return true;
        }

        float baseDamage = 20.0f * Math.max(1, amplifier);
        for (LivingEntity victim : victims) {
            float damage = applyFrostResistance(victim, level, baseDamage);
            if (damage > 0.0f) {
                victim.hurtServer(level, level.damageSources().mobAttack(entity), damage);
            }
        }

        Main.LOGGER.info("[frost] sliding frozen mob hit {} entity(ies): baseDamage={}", victims.size(), baseDamage);

        level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY() + 0.8, entity.getZ(),
                24, 0.4, 0.6, 0.4, 0.02);
        level.sendParticles(ParticleTypes.SPLASH, entity.getX(), entity.getY() + 0.8, entity.getZ(),
                12, 0.35, 0.5, 0.35, 0.01);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.5f, 0.5f);

        entity.setDeltaMovement(Vec3.ZERO);
        entity.hurtMarked = true;
        return true;
    }

    private static float applyFrostResistance(LivingEntity victim, ServerLevel level, float damage) {
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var holder = registry.get(FROST_RESISTANCE);
        if (holder.isEmpty()) {
            return damage;
        }
        int totalLevel = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            totalLevel += EnchantmentHelper.getItemEnchantmentLevel(holder.get(), victim.getItemBySlot(slot));
        }
        // Każdy poziom Frost Resistance zmniejsza obrażenia o 20% (IV na jednej części = 80%):
        float reduction = Math.min(1.0f, totalLevel * 0.2f);
        return damage * (1.0f - reduction);
    }
}
