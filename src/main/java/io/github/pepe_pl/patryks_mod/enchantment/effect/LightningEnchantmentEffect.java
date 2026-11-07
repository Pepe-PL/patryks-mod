package io.github.pepe_pl.patryks_mod.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.NullMarked;

import io.github.pepe_pl.patryks_mod.HitParticleRings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

@NullMarked
public record LightningEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<LightningEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(LightningEnchantmentEffect::amount)
            ).apply(instance, LightningEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (target instanceof LivingEntity victim) {
            if (context.owner() instanceof Player) {
                // 1. Uderzenie piorunem zależnie od poziomu zaklęcia:
                float numStrikes = this.amount.calculate(level);
                BlockPos position = victim.blockPosition();

                for (float i = 0; i < numStrikes; i++) {
                    EntityTypes.LIGHTNING_BOLT.spawn(serverLevel, position, EntitySpawnReason.TRIGGERED);
                }

                // 2. Wiele koncentrycznych okręgów – każdy w innym kolorze:
                HitParticleRings.spawn(serverLevel, victim.getX(), victim.getY() + 0.2, victim.getZ());

                // 3. Efekt dźwiękowy rezonansu kryształu ametystu:
                serverLevel.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                        SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0f, 1.2f);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
