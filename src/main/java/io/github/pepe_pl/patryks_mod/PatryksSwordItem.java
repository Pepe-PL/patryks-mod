package io.github.pepe_pl.patryks_mod;

import java.util.List;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@NullMarked
@SuppressWarnings({"deprecation", "resource"})
public class PatryksSwordItem extends Item {
    private static final double ABILITY_RANGE = 28.0;
    private static final float ABILITY_DAMAGE = 250.0f;
    private static final ResourceKey<Enchantment> FROST_ASPECT = ResourceKey.create(
            Registries.ENCHANTMENT, Main.id("frost_aspect"));

    public PatryksSwordItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        // Miecz zawsze ma Frost Aspect II – także z creative tab, /give i stare egzemplarze:
        level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(FROST_ASPECT).ifPresent(holder -> {
            if (EnchantmentHelper.getItemEnchantmentLevel(holder, stack) < 2) {
                EnchantmentHelper.updateEnchantments(stack, mutable -> mutable.set(holder, 2));
            }
        });
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // 1. Silny, długi zryw w kierunku wzroku – bez cooldownu, można spamować w locie:
        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(look.x * 3.6, Math.max(0.55, look.y * 2.2 + 0.35), look.z * 3.6);
        player.hurtMarked = true;
        player.resetFallDistance();

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2.0f, 1.2f);
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.5f, 0.8f);

            // 2. Fala soniczna leci dalej (28 bloków):
            int steps = (int) ABILITY_RANGE;
            for (int i = 1; i <= steps; i++) {
                double px = player.getX() + look.x * i;
                double py = player.getEyeY() + look.y * i;
                double pz = player.getZ() + look.z * i;

                if (i % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                }
                serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 8, 0.25, 0.25, 0.25, 0.05);
            }

            AABB shockwaveArea = player.getBoundingBox().expandTowards(look.scale(ABILITY_RANGE)).inflate(2.8);
            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, shockwaveArea,
                    e -> e != player && e.isAlive());

            for (LivingEntity target : targets) {
                target.invulnerableTime = 0;
                target.hurtServer(serverLevel, serverLevel.damageSources().playerAttack(player), ABILITY_DAMAGE);
                target.setDeltaMovement(target.getDeltaMovement().add(look.x * 2.2, 0.75, look.z * 2.2));

                if (target.isDeadOrDying()) {
                    HitParticleRings.spawn(serverLevel, target.getX(), target.getY() + 0.2, target.getZ());
                    KillCelebration.spawn(serverLevel, target.getX(), target.getY(), target.getZ());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.1").withStyle(ChatFormatting.GOLD));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.2").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("itemTooltip.patryks-mod.patryks_sword.3").withStyle(ChatFormatting.GRAY));
    }
}
