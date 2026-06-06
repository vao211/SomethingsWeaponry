package vao211.somethingsweaponry.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class SpearItem extends SwordItem {
    public SpearItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (hand == Hand.OFF_HAND) {
            return TypedActionResult.pass(stack);
        }

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            ItemStack offhandStack = user.getStackInHand(Hand.OFF_HAND);
            if (!offhandStack.isEmpty()) {
                user.setCurrentHand(Hand.OFF_HAND);
                return offhandStack.getItem().use(world, user, Hand.OFF_HAND);
            }
            return TypedActionResult.fail(stack);
        }

        Vec3d startPos = user.getCameraPosVec(1.0F);
        Vec3d lookVec = user.getRotationVec(1.0F);
        double spearRange = 5.0;
        Vec3d endPos = startPos.add(lookVec.multiply(spearRange));

        Box searchBox = user.getBoundingBox().stretch(lookVec.multiply(spearRange)).expand(1.0);

        LivingEntity firstTarget = null;
        double minDistanceSq = spearRange * spearRange;

        for (Entity entity : world.getOtherEntities(user, searchBox, e -> e instanceof LivingEntity && e.isAlive())) {
            Box entityHitbox = entity.getBoundingBox().expand(0.3f);
            Optional<Vec3d> hitPoint = entityHitbox.raycast(startPos, endPos);

            if (hitPoint.isPresent()) {
                double distSq = startPos.squaredDistanceTo(hitPoint.get());
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    firstTarget = (LivingEntity) entity;
                }
            }
        }

        float rawDamage = (float) user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if (firstTarget != null) {
            final LivingEntity finalFirstTarget = firstTarget;
            finalFirstTarget.damage(world.getDamageSources().playerAttack(user), rawDamage);

            finalFirstTarget.takeKnockback(1.0, -lookVec.x, -lookVec.z);

            Box pierceBox = finalFirstTarget.getBoundingBox().expand(4.0);

            List<LivingEntity> behindTargets = world.getEntitiesByClass(LivingEntity.class, pierceBox,
                    e -> e != user && e != finalFirstTarget && e.isAlive());

            Vec3d lineStart = finalFirstTarget.getPos();

            for (LivingEntity target : behindTargets) {
                Vec3d diff = target.getPos().subtract(lineStart);
                double distanceAlongLine = diff.dotProduct(lookVec);

                if (distanceAlongLine > 0 && distanceAlongLine <= 3.0) {
                    Vec3d projectedPoint = lineStart.add(lookVec.multiply(distanceAlongLine));
                    double distanceToLine = target.getPos().distanceTo(projectedPoint);

                    if (distanceToLine <= 1.5) {
                        double distance = finalFirstTarget.distanceTo(target);
                        float reduction = (float) Math.floor(distance) * 0.2f;

                        if (reduction > 0.6f) {
                            reduction = 0.6f;
                        }

                        float finalDamage = rawDamage * (1.0f - reduction);
                        target.damage(world.getDamageSources().playerAttack(user), finalDamage);
                        target.takeKnockback(1.0, -lookVec.x, -lookVec.z);
                    }
                }
            }
        }

        user.getItemCooldownManager().set(this, 80);

        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;

            for (int i = 1; i <= 15; i++) {
                Vec3d particlePos = startPos.add(lookVec.multiply(i * 0.33));
                serverWorld.spawnParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0);
            }

            serverWorld.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0f, 1.5f);

            if (firstTarget != null) {
                serverWorld.playSound(null, firstTarget.getBlockPos(), SoundEvents.ITEM_TRIDENT_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }

        ItemStack offhandStack = user.getStackInHand(Hand.OFF_HAND);
        if (!offhandStack.isEmpty()) {
            user.setCurrentHand(Hand.OFF_HAND);
            offhandStack.getItem().use(world, user, Hand.OFF_HAND);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}