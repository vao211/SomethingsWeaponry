package vao211.somethingsweaponry;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import vao211.somethingsweaponry.config.WeaponConfig;
import vao211.somethingsweaponry.registry.ModItemGroups;
import vao211.somethingsweaponry.registry.ModItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Somethingsweaponry implements ModInitializer {
    public static final String MOD_ID = "somethingsweaponry";
    @Override
    public void onInitialize() {
        Path configDir = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("somethingsweaponry");
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MidnightConfig.init("somethingsweaponry/weapon_config", WeaponConfig.class);
        ModItems.registerModItems();
        ModItemGroups.registerModItemGroups();
    }
}
