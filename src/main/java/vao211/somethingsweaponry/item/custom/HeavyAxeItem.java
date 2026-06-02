package vao211.somethingsweaponry.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vao211.somethingsweaponry.util.IEmpoweredEntity;

public class HeavyAxeItem extends AxeItem {
    public HeavyAxeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (hand == Hand.OFF_HAND) return TypedActionResult.pass(stack);
        if (user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(stack);

        IEmpoweredEntity empoweredUser = (IEmpoweredEntity) user;

        if (empoweredUser.somethingsweaponry$getEmpoweredTicks() > 0) {
            return TypedActionResult.fail(stack);
        }
        empoweredUser.somethingsweaponry$setEmpoweredTicks(100);

        user.swingHand(hand);

        return TypedActionResult.success(stack, world.isClient());
    }
}