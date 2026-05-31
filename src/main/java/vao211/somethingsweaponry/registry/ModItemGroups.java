package vao211.somethingsweaponry.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup SOMETHINGS_WEAPONRY_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of("somethingsweaponry", "main_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.OBLIVION_INGOT))
                    .displayName(Text.translatable("itemGroup.somethingsweaponry.main_group"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.OBLIVION_INGOT);
                        entries.add(ModItems.OBLIVION_SWORD);
                        entries.add(ModItems.OBLIVION_DAGGER);
                        entries.add(ModItems.OBLIVION_AXE);
                    })
                    .build()
    );
    public static void registerModItemGroups() {
    }
}
