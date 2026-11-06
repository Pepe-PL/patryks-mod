package io.github.pepe_pl.patryks_mod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class Main implements ModInitializer {
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath("patryks-mod", path);
	}

	@Override
	public void onInitialize() {
	}
}