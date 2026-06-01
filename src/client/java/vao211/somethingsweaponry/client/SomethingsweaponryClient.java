package vao211.somethingsweaponry.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import vao211.somethingsweaponry.registry.ModItems;

public class SomethingsweaponryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(ModItems.OBLIVION_BLADE, Identifier.ofVanilla("blocking"),
                (stack, world, entity, seed) -> {
                    return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
                }
        );
    }
}
