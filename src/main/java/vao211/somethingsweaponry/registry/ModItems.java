package vao211.somethingsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import vao211.somethingsweaponry.Somethingsweaponry;
import vao211.somethingsweaponry.config.WeaponConfig;
import vao211.somethingsweaponry.item.ModToolMaterials;
import vao211.somethingsweaponry.item.OblivionSword;

public class ModItems {

    public static final Item OBLIVION_INGOT = new Item(new Item.Settings());

    public static final Item OBLIVION_SWORD = new OblivionSword(new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionSwordDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionSwordAttackSpeed)
            )
    ));
    public static void registerModItems() {
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_ingot"), OBLIVION_INGOT);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_sword"), OBLIVION_SWORD);
    }
    private static int calcDamage(float finalDamage, float materialDamage) {
        return (int) (finalDamage - materialDamage - 1.0f);
    }
    private static float calcSpeed(float finalSpeed) {
        return finalSpeed - 4.0f;
    }
}