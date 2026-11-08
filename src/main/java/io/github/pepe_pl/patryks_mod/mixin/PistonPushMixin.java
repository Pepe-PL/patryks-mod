package io.github.pepe_pl.patryks_mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.pepe_pl.patryks_mod.effect.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(PistonBaseBlock.class)
public abstract class PistonPushMixin {
    @Inject(method = "triggerEvent", at = @At("TAIL"))
    private void patryksMod$pushFrozenMobs(BlockState state, Level level, BlockPos pos, int eventId, int eventParam, CallbackInfoReturnable<Boolean> cir) {
        if (eventId != PistonBaseBlock.TRIGGER_EXTEND || level.isClientSide()) {
            return;
        }

        Direction facing = state.getValue(BlockStateProperties.FACING);
        BlockPos front = pos.relative(facing);

        // Obszar przed głowicą pistona (2 bloki w głąb):
        AABB pushZone = new AABB(front)
                .expandTowards(facing.getStepX(), 0, facing.getStepZ())
                .inflate(0.3)
                .expandTowards(0, 2, 0);

        Vec3 push = new Vec3(facing.getStepX() * 1.2, 0.25, facing.getStepZ() * 1.2);

        var frozen = level.getEntitiesOfClass(LivingEntity.class, pushZone,
                e -> e.hasEffect(ModMobEffects.FROST_FROZEN));
        if (!frozen.isEmpty()) {
            io.github.pepe_pl.patryks_mod.Main.LOGGER.info("[frost] piston at {} facing {} pushing {} frozen mob(s)", pos, facing, frozen.size());
        }
        for (LivingEntity entity : frozen) {
            entity.setDeltaMovement(push);
            entity.hurtMarked = true;
        }
    }
}
