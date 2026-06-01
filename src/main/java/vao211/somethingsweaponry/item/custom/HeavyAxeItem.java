package vao211.somethingsweaponry.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HeavyAxeItem extends AxeItem {
    //Right click to empowerd 1 attack (x2 dmg)
    //Login in PlayerEntityMixin
    public HeavyAxeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hand == Hand.OFF_HAND) {
            return TypedActionResult.pass(stack);
        }
        if(user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("Empowered",true);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5f, 1.8f);
        return TypedActionResult.success(stack, world.isClient());
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            return customData.getNbt().getBoolean("Empowered");
        }
        return super.hasGlint(stack);
    }
}