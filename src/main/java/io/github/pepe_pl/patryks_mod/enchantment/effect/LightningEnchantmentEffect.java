package io.github.pepe_pl.patryks_mod.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

@SuppressWarnings({"NullableProblems", "unused"})
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

                // 2. Magiczny, świecący okrąg cząsteczek w odcieniach błękitu i fioletu:
                double radius = 2.5;
                int points = 36;
                for (int i = 0; i < points; i++) {
                    double angle = 2 * Math.PI * i / points;
                    double px = victim.getX() + radius * Math.cos(angle);
                    double py = victim.getY() + 0.2;
                    double pz = victim.getZ() + radius * Math.sin(angle);

                    // Błękitny płomień dusz (cyan/blue):
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz, 1, 0.0, 0.05, 0.0, 0.01);
                    // Ciemnofioletowe cząsteczki portalu:
                    serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 3, 0.05, 0.05, 0.05, 0.02);
                    // Magiczne cząsteczki czarownicy (fioletowo-różowe):
                    serverLevel.sendParticles(ParticleTypes.WITCH, px, py, pz, 1, 0.0, 0.05, 0.0, 0.01);
                    // Świecące iskry (glowing cyan):
                    serverLevel.sendParticles(ParticleTypes.GLOW, px, py + 0.25, pz, 1, 0.05, 0.05, 0.05, 0.02);
                }

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
