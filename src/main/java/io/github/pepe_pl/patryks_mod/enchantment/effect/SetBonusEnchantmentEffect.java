package io.github.pepe_pl.patryks_mod.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jspecify.annotations.NullMarked;

import io.github.pepe_pl.patryks_mod.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

@NullMarked
public record SetBonusEnchantmentEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {
    public static final MapCodec<SetBonusEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("duration").forGetter(SetBonusEnchantmentEffect::duration)
            ).apply(instance, SetBonusEnchantmentEffect::new)
    );

    private static boolean isPatryksArmor(Item item, Item expected) {
        return item == expected;
    }

    private static boolean wearsFullSet(LivingEntity wearer) {
        return isPatryksArmor(wearer.getItemBySlot(EquipmentSlot.HEAD).getItem(), ModItems.PATRYKS_HELMET)
                && isPatryksArmor(wearer.getItemBySlot(EquipmentSlot.CHEST).getItem(), ModItems.PATRYKS_CHESTPLATE)
                && isPatryksArmor(wearer.getItemBySlot(EquipmentSlot.LEGS).getItem(), ModItems.PATRYKS_LEGGINGS)
                && isPatryksArmor(wearer.getItemBySlot(EquipmentSlot.FEET).getItem(), ModItems.PATRYKS_BOOTS);
    }

    @Override
    public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        LivingEntity wearer = context.owner();
        if (wearer == null || !wearer.isAlive() || !wearsFullSet(wearer)) {
            return;
        }

        int durationTicks = Math.max(20, (int) this.duration.calculate(level));

        // Zawsze przy pełnym zestawie: Regeneracja + Odporność na ogień
        int regenAmp = Math.min(level, 2) - 1; // poziom I -> Regeneracja I, II/III -> Regeneracja II
        wearer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, durationTicks, regenAmp, false, false, true));
        wearer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, durationTicks, 0, false, false, true));

        // Poziom II+: Siła
        if (level >= 2) {
            wearer.addEffect(new MobEffectInstance(MobEffects.STRENGTH, durationTicks, 0, false, false, true));
        }

        // Poziom III: dodatkowo Odporność (Resistance)
        if (level >= 3) {
            wearer.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, durationTicks, 0, false, false, true));
        }

        // Fioletowy pył wokół noszącego jako informacja o aktywnym buście:
        serverLevel.sendParticles(ParticleTypes.WITCH, wearer.getX(), wearer.getY() + 1.0, wearer.getZ(),
                4, 0.3, 0.5, 0.3, 0.01);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
