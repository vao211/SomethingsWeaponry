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

public class BattleHammerItem extends AxeItem {
    //Right Click to get Empowered attack (x1.5 dmg, AOE 2x2 + Stun)
    //Logic in EmpoweredWeaponMixin, EmpoweredWeaponVictimMixin
    public BattleHammerItem(ToolMaterial toolMaterial, Settings settings) {
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

        empoweredUser.somethingsweaponry$setEmpoweredTicks(80);

        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.spawnParticles(ParticleTypes.CRIT, user.getX(), user.getY() + 1.0, user.getZ(), 40, 0.5, 0.5, 0.5, 0.1);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_DEEPSLATE_BREAK, SoundCategory.PLAYERS, 1.0f, 0.5f);
        }
        user.swingHand(hand);
        return TypedActionResult.success(stack, world.isClient());
    }
}
