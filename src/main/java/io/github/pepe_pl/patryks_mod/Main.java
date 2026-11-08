package io.github.pepe_pl.patryks_mod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("patryks-mod");

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath("patryks-mod", path);
	}

	@Override
	public void onInitialize() {
		ModItems.initialize();
		io.github.pepe_pl.patryks_mod.enchantment.ModEnchantmentEffects.initialize();
		io.github.pepe_pl.patryks_mod.effect.ModMobEffects.initialize();
	}
}