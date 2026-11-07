package io.github.pepe_pl.patryks_mod;

import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.phys.Vec3;

/**
 * Efekty po zabiciu: kolorowe orby + krótkie fajerwerki.
 */
public final class KillCelebration {
    private static final int[] ORB_COLORS = {
            0xFF1744, 0xFF9100, 0xFFEA00, 0x00E676,
            0x00E5FF, 0x2979FF, 0xD500F9, 0xFF4081
    };

    private KillCelebration() {
    }

    public static void spawn(ServerLevel level, double x, double y, double z) {
        spawnColorfulOrbs(level, x, y + 0.5, z);
        spawnShortFireworks(level, x, y + 0.8, z);
    }

    private static void spawnColorfulOrbs(ServerLevel level, double x, double y, double z) {
        // Prawdziwe orby XP rozlatujące się na boki:
        for (int i = 0; i < 12; i++) {
            double angle = 2 * Math.PI * i / 12.0;
            Vec3 pos = new Vec3(x, y, z);
            Vec3 dir = new Vec3(Math.cos(angle) * 0.35, 0.25 + (i % 3) * 0.08, Math.sin(angle) * 0.35);
            ExperienceOrb.awardWithDirection(level, pos, dir, 3 + (i % 5));
        }

        // Kolorowe „kule” z pyłu wokół ofiary:
        for (int orb = 0; orb < ORB_COLORS.length; orb++) {
            double angle = 2 * Math.PI * orb / ORB_COLORS.length;
            double ox = x + Math.cos(angle) * 1.4;
            double oy = y + 0.4 + (orb % 2) * 0.35;
            double oz = z + Math.sin(angle) * 1.4;
            DustParticleOptions dust = new DustParticleOptions(ORB_COLORS[orb], 1.6f);

            for (int p = 0; p < 18; p++) {
                double theta = (p / 18.0) * Math.PI * 2;
                double phi = ((p * 7) % 18) / 18.0 * Math.PI;
                double r = 0.25;
                level.sendParticles(
                        dust,
                        ox + r * Math.sin(phi) * Math.cos(theta),
                        oy + r * Math.cos(phi),
                        oz + r * Math.sin(phi) * Math.sin(theta),
                        1, 0.0, 0.0, 0.0, 0.0
                );
            }
        }
    }

    private static void spawnShortFireworks(ServerLevel level, double x, double y, double z) {
        // flightDuration = 0 → eksplozja niemal od razu (krótkie fajerwerki)
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        rocket.set(DataComponents.FIREWORKS, new Fireworks(
                0,
                List.of(
                        new FireworkExplosion(
                                FireworkExplosion.Shape.BURST,
                                IntList.of(0xFF1744, 0xFFEA00, 0x00E676, 0x2979FF, 0xD500F9, 0xFF4081),
                                IntList.of(0xFFFFFF),
                                true,
                                true
                        ),
                        new FireworkExplosion(
                                FireworkExplosion.Shape.SMALL_BALL,
                                IntList.of(0x00E5FF, 0xFF9100, 0xFF4081),
                                IntList.of(),
                                false,
                                true
                        )
                )
        ));

        double[][] offsets = {
                {0.0, 0.2, 0.0},
                {0.7, 0.4, -0.5},
                {-0.6, 0.5, 0.6}
        };
        for (double[] offset : offsets) {
            level.addFreshEntity(new FireworkRocketEntity(
                    level, x + offset[0], y + offset[1], z + offset[2], rocket.copy()));
        }
    }
}
