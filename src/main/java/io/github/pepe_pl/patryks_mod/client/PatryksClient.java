package io.github.pepe_pl.patryks_mod.client;

import io.github.pepe_pl.patryks_mod.Main;
import io.github.pepe_pl.patryks_mod.mixin.SpecialModelRenderersAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.object.equipment.ShieldModel;

public class PatryksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpecialModelRenderersAccessor.patryks$getIdMapper().put(Main.id("patryks_shield"), PatryksShieldSpecialRenderer.Unbaked.MAP_CODEC);
        ModelLayerRegistry.registerModelLayer(PatryksShieldLayers.PATRYKS_SHIELD, ShieldModel::createLayer);
    }
}
