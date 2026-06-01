package vao211.somethingsweaponry.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BladeItem extends SwordItem {
    //Block attack for 3 attack in 3s (cd: 2s)
    //Logic in PlayerEntityMixin
    public BladeItem(ToolMaterial toolMaterial, Settings settings){
        super(toolMaterial, settings);
    }
    @Override
    public UseAction getUseAction(ItemStack stack){
        //Block attack
        return UseAction.BLOCK;
    }
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 60;
    }

    //activate block
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (hand == Hand.OFF_HAND) {
            return TypedActionResult.pass(itemStack);
        }
        if (!user.getOffHandStack().isEmpty()) {
            return TypedActionResult.fail(itemStack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 40);
        }
        return super.finishUsing(stack, world, user);
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 40);
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }
}
