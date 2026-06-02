package vao211.somethingsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vao211.somethingsweaponry.item.custom.HeavyAxeItem;
import vao211.somethingsweaponry.util.IEmpoweredEntity;

@Mixin(LivingEntity.class)
public abstract class HeavyAxeVictimMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true, index = 2)
    private float somethingsweaponry$applyHeavyAxeDamage(float amount, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();

            if (weapon.getItem() instanceof HeavyAxeItem heavyAxe) {
                IEmpoweredEntity empoweredAttacker = (IEmpoweredEntity) attacker;

                if (empoweredAttacker.somethingsweaponry$getEmpoweredTicks() > 0) {
                    attacker.getItemCooldownManager().set(heavyAxe, 100);
                    empoweredAttacker.somethingsweaponry$setEmpoweredTicks(0);
                    if (!victim.getWorld().isClient()) {
                        ServerWorld serverWorld = (ServerWorld) victim.getWorld();
                        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, victim.getX(), victim.getBodyY(0.5), victim.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                        serverWorld.playSound(null, victim.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 1.2f);
                    }
                    return amount * 2.0f;
                }
            }
        }
        return amount;
    }
}