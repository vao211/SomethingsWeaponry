package vao211.somethingsweaponry.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HalberdItem extends SwordItem {
    public HalberdItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (hand == Hand.OFF_HAND) {
            return TypedActionResult.pass(stack);
        }

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        float rawDamage = (float) user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        Box aoeBox = user.getBoundingBox().expand(4.0);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, aoeBox,
                e -> e != user && e.isAlive());

        boolean hitAny = false;

        for (LivingEntity target : targets) {
            if (user.distanceTo(target) <= 4.0) {
                target.damage(world.getDamageSources().playerAttack(user), rawDamage);
                double dX = user.getX() - target.getX();
                double dZ = user.getZ() - target.getZ();
                target.takeKnockback(1.5, dX, dZ);

                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 2, false, true, true));

                hitAny = true;
            }
        }

        user.getItemCooldownManager().set(this, 160);


        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;

            for (int i = 0; i < 360; i += 15) {
                double rad = Math.toRadians(i);
                double x = user.getX() + Math.cos(rad) * 2.5;
                double z = user.getZ() + Math.sin(rad) * 2.5;

                serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, user.getBodyY(0.5), z, 1, 0, 0, 0, 0);
            }

            serverWorld.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 0.8f);
            serverWorld.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.PLAYERS, 1.0f, 0.5f);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}