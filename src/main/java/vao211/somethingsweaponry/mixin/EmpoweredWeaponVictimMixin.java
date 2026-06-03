package vao211.somethingsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vao211.somethingsweaponry.item.custom.BattleHammerItem;
import vao211.somethingsweaponry.item.custom.HeavyAxeItem;
import vao211.somethingsweaponry.util.IEmpoweredEntity;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class EmpoweredWeaponVictimMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true, index = 2)
    private float somethingsweaponry$applyEmpoweredDamage(float amount, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();
            IEmpoweredEntity empoweredAttacker = (IEmpoweredEntity) attacker;

            if (empoweredAttacker.somethingsweaponry$getEmpoweredTicks() > 0) {
                float rawDamage = (float) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                if (weapon.getItem() instanceof HeavyAxeItem heavyAxe) {
                    attacker.getItemCooldownManager().set(heavyAxe, 100);
                    empoweredAttacker.somethingsweaponry$setEmpoweredTicks(0);

                    if (!victim.getWorld().isClient()) {
                        ServerWorld serverWorld = (ServerWorld) victim.getWorld();
                        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, victim.getX(), victim.getBodyY(0.5), victim.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                        serverWorld.playSound(null, victim.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 1.2f);
                    }

                    return rawDamage * 2.0f;
                }

                else if (weapon.getItem() instanceof BattleHammerItem battleHammer) {
                    attacker.getItemCooldownManager().set(battleHammer, 100);
                    empoweredAttacker.somethingsweaponry$setEmpoweredTicks(0);

                    if (!victim.getWorld().isClient()) {
                        ServerWorld serverWorld = (ServerWorld) victim.getWorld();

                        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, victim.getX(), victim.getY(), victim.getZ(), 40, 1.5, 0.2, 1.5, 0.05);
                        serverWorld.playSound(null, victim.getBlockPos(), SoundEvents.ENTITY_IRON_GOLEM_DEATH, SoundCategory.PLAYERS, 1.0f, 0.5f);

                        Box aoeBox = victim.getBoundingBox().expand(2.0);
                        List<LivingEntity> targets = serverWorld.getEntitiesByClass(LivingEntity.class, aoeBox, e -> e != attacker && e.isAlive());

                        for (LivingEntity target : targets) {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 5, false, true, true));
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 40, 5, false, true, true));
                        }
                    }
                    return rawDamage * 1.5f;
                }
            }
        }
        return amount;
    }
}