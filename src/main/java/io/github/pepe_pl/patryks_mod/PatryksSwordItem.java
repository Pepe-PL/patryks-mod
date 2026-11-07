package io.github.pepe_pl.patryks_mod;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings({"NullableProblems", "deprecation", "resource"})
public class PatryksSwordItem extends Item {
    public PatryksSwordItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 1. Zryw (Dash) w kierunku wzroku gracza:
        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(look.x * 2.2, Math.max(0.4, look.y * 1.5 + 0.2), look.z * 2.2);
        player.hurtMarked = true;
        player.resetFallDistance();

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            // 2. Dźwięki fali dźwiękowej Wardena oraz kryształu ametystu:
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2.0f, 1.2f);
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.5f, 0.8f);

            // 3. Cząsteczki fali sonicznej i ametystowego portalu wzdłuż wektora ataku:
            for (int i = 1; i <= 12; i++) {
                double px = player.getX() + look.x * i;
                double py = player.getEyeY() + look.y * i;
                double pz = player.getZ() + look.z * i;

                if (i % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                }
                serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 8, 0.25, 0.25, 0.25, 0.05);
            }

            // 4. Zadawanie 250 HP obrażeń i odrzucenie wrogów w obszarze fali:
            AABB shockwaveArea = player.getBoundingBox().expandTowards(look.scale(12.0)).inflate(2.5);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, shockwaveArea,
                    e -> e != player && e.isAlive());

            for (LivingEntity target : targets) {
                target.invulnerableTime = 0;
                target.hurtServer(serverLevel, serverLevel.damageSources().playerAttack(player), 250.0f);
                target.setDeltaMovement(target.getDeltaMovement().add(look.x * 1.8, 0.6, look.z * 1.8));
            }

            // 5. Cooldown: 2.5 sekundy (50 ticków):
            player.getCooldowns().addCooldown(stack, 50);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        // Reset klatek nietykalności celu po uderzeniu - autokliker może zadawać pełne obrażenia bez opóźnień:
        target.invulnerableTime = 0;

        // Wbudowany na stałe efekt pioruna oraz okręgu błękitno-fioletowych cząsteczek przy każdym trafieniu:
        if (!attacker.level().isClientSide() && attacker.level() instanceof ServerLevel serverLevel) {
            EntityTypes.LIGHTNING_BOLT.spawn(serverLevel, target.blockPosition(), EntitySpawnReason.TRIGGERED);

            double radius = 2.5;
            int points = 36;
            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI * i / points;
                double px = target.getX() + radius * Math.cos(angle);
                double py = target.getY() + 0.2;
                double pz = target.getZ() + radius * Math.sin(angle);

                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz, 1, 0.0, 0.05, 0.0, 0.01);
                serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 3, 0.05, 0.05, 0.05, 0.02);
                serverLevel.sendParticles(ParticleTypes.WITCH, px, py, pz, 1, 0.0, 0.05, 0.0, 0.01);
                serverLevel.sendParticles(ParticleTypes.GLOW, px, py + 0.25, pz, 1, 0.05, 0.05, 0.05, 0.02);
            }

            serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0f, 1.2f);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.1").withStyle(ChatFormatting.GOLD));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.2").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.3").withStyle(ChatFormatting.GRAY));
    }
}
