package vao211.somethingsweaponry.registry;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import vao211.somethingsweaponry.Somethingsweaponry;
import vao211.somethingsweaponry.config.WeaponConfig;
import vao211.somethingsweaponry.item.*;
import vao211.somethingsweaponry.item.custom.HalberdItem;

public class ModItems {
    public static final Item OBLIVION_INGOT = new Item(new Item.Settings());

    public static final Item OBLIVION_SWORD = new OblivionSword(new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionSwordDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionSwordAttackSpeed)
            )
    ));
    public static final Item OBLIVION_DAGGER = new OblivionDagger(new Item.Settings().attributeModifiers(
            SwordItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionDaggerDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionDaggerAttackSpeed)
                    )
    ));
    public static final Item OBLIVION_AXE = new OblivionAxe(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionAxeDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionAxeAttackSpeed)
            )
    ));
    public static final Item OBLIVION_BLADE = new OblivionBlade(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionBladeDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionBladeAttackSpeed)
            )
    ));

    public static final Item OBLIVION_HEAVY_AXE = new OblivionHeavyAxe(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionHeavyAxeDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionHeavyAxeAttackSpeed)
            )
    ));

    public static final Item OBLIVION_HEAVY_SWORD = new OblivionHeavySword(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionHeavySwordDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionHeavySwordAttackSpeed)
            )
    ));
    public static final Item OBLIVION_BATTLE_HAMMER = new OblivionBattleHammer(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionBattleHammerDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionBattleHammerAttackSpeed)
            )
    ));
    public static final Item OBLIVION_SPEAR = new OblivionSpear(new Item.Settings().attributeModifiers(
            AxeItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionSpearDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionSpearAttackSpeed)
            )
    ));
    public static final Item OBLIVION_TRIDENT = new OblivionTrident(new Item.Settings()
            .maxDamage(ModToolMaterials.OBLIVION.getDurability())
            .attributeModifiers(
                    SwordItem.createAttributeModifiers(
                            ModToolMaterials.OBLIVION,
                            calcDamage(WeaponConfig.oblivionTridentDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                            calcSpeed(WeaponConfig.oblivionTridentAttackSpeed)
                    )
            )
    );
    public static final Item OBLIVION_HALBERD = new OblivionHalberd(new Item.Settings().attributeModifiers(
            HalberdItem.createAttributeModifiers(
                    ModToolMaterials.OBLIVION,
                    calcDamage(WeaponConfig.oblivionHalberdDamage, ModToolMaterials.OBLIVION.getAttackDamage()),
                    calcSpeed(WeaponConfig.oblivionHalberdAttackSpeed)
            )
    ));
    public static void registerModItems() {
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_ingot"), OBLIVION_INGOT);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_sword"), OBLIVION_SWORD);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_dagger"), OBLIVION_DAGGER);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID,"oblivion_axe"), OBLIVION_AXE);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID,"oblivion_blade"), OBLIVION_BLADE);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID,"oblivion_heavy_axe"), OBLIVION_HEAVY_AXE);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID,"oblivion_heavy_sword"), OBLIVION_HEAVY_SWORD);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID,"oblivion_battle_hammer"), OBLIVION_BATTLE_HAMMER);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_spear"), OBLIVION_SPEAR);
        Registry.register(Registries.ITEM, Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_trident"), OBLIVION_TRIDENT);
        Registry.register(Registries.ITEM,Identifier.of(Somethingsweaponry.MOD_ID, "oblivion_halberd"), OBLIVION_HALBERD);
    }

    private static int calcDamage(float finalDamage, float materialDamage) {
        return (int) (finalDamage - materialDamage - 1.0f);
    }
    private static float calcSpeed(float finalSpeed) {
        return finalSpeed - 4.0f;
    }
}