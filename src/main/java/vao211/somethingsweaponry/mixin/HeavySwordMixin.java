package vao211.somethingsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vao211.somethingsweaponry.item.custom.HeavySwordItem;

@Mixin(LivingEntity.class)
public abstract class HeavySwordMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true, index = 2)
    private float somethingsweaponry$applyHeavySwordPassive(float amount, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();
            if (weapon.getItem() instanceof HeavySwordItem heavySword) {

                if (!attacker.getItemCooldownManager().isCoolingDown(heavySword)) {
                    attacker.getItemCooldownManager().set(heavySword, 80);

                    if (victim.isBlocking() && victim instanceof PlayerEntity targetPlayer) {
                        Item activeShield = targetPlayer.getActiveItem().getItem();
                        targetPlayer.getItemCooldownManager().set(activeShield, 100);
                        targetPlayer.clearActiveItem();
                        targetPlayer.getWorld().playSound(null, targetPlayer.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 1.0f, 0.8f + targetPlayer.getWorld().random.nextFloat() * 0.4f);
                    }

                    if (!victim.getWorld().isClient()) {
                        ServerWorld serverWorld = (ServerWorld) victim.getWorld();
                        serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, victim.getX(), victim.getBodyY(0.5), victim.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                        serverWorld.playSound(null, victim.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 0.7f);
                    }

                    return amount * 1.5f;
                }
            }
        }
        return amount;
    }
}