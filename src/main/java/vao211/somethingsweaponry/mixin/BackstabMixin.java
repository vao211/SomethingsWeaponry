package vao211.somethingsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vao211.somethingsweaponry.item.custom.DaggerItem;

@Mixin(LivingEntity.class)
public abstract class BackstabMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float somethingsweaponry$applyBackstabDamage(float amount, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if (source.getAttacker() instanceof PlayerEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();

            if (weapon.getItem() instanceof DaggerItem) {
                Vec3d victimLook = victim.getRotationVec(1.0F).normalize();
                Vec3d victimToAttacker = attacker.getPos().subtract(victim.getPos()).normalize();
                double dotProduct = (victimLook.x * victimToAttacker.x) + (victimLook.z * victimToAttacker.z);

                //120 deg on back(dotProduct < -0.5)
                if (dotProduct < -0.5) {
                    if (!victim.getWorld().isClient()) {
                        ServerWorld serverWorld = (ServerWorld) victim.getWorld();
                        serverWorld.spawnParticles(ParticleTypes.DAMAGE_INDICATOR, victim.getX(), victim.getBodyY(0.5), victim.getZ(), 10, 0.2, 0.2, 0.2, 0.1);
                        serverWorld.playSound(null, victim.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.3f);
                    }
                    return amount * 1.3f;
                }
            }
        }
        return amount;
    }
}