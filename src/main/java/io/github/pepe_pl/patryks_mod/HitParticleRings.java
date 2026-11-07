package io.github.pepe_pl.patryks_mod;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;

/**
 * Koncentryczne okręgi cząsteczek – każdy pierścień w innym kolorze.
 */
public final class HitParticleRings {
    // Kolory RGB (0xRRGGBB) od środka na zewnątrz:
    private static final int[] RING_COLORS = {
            0xFF1744, // czerwony
            0xFF9100, // pomarańczowy
            0xFFEA00, // żółty
            0x00E676, // zielony
            0x00E5FF, // cyjan
            0x2979FF, // niebieski
            0xD500F9, // fioletowy
            0xFF4081  // różowy
    };

    private HitParticleRings() {
    }

    public static void spawn(ServerLevel level, double x, double y, double z) {
        int points = 40;
        float scale = 1.25f;

        for (int ring = 0; ring < RING_COLORS.length; ring++) {
            double radius = 1.0 + ring * 0.55;
            DustParticleOptions dust = new DustParticleOptions(RING_COLORS[ring], scale);

            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI * i / points;
                double px = x + radius * Math.cos(angle);
                double pz = z + radius * Math.sin(angle);
                level.sendParticles(dust, px, y, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }
}
