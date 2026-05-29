package vao211.somethingsweaponry.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import vao211.somethingsweaponry.Somethingsweaponry;
import vao211.somethingsweaponry.registry.ModItems;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    OBLIVION(2000, 9.0F, 6.0F, 15, () -> Ingredient.ofItems(ModItems.OBLIVION_INGOT));

    public static final TagKey<Block> INCORRECT_FOR_OBLIVION_TOOL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Somethingsweaponry.MOD_ID, "incorrect_for_oblivion_tool"));

    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }
    @Override
    public TagKey<Block> getInverseTag() {
        return INCORRECT_FOR_OBLIVION_TOOL;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}