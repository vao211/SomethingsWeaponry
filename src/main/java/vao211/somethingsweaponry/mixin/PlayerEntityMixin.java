package vao211.somethingsweaponry.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vao211.somethingsweaponry.item.custom.BladeItem;
import vao211.somethingsweaponry.item.custom.HeavyAxeItem;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin {
    @Unique
    private int somethingsweaponry$bladeBlockCount = 0;

    //-----------------------
    // Blade blocking attack
    //-----------------------
    @Inject(method = "stopUsingItem", at = @At("HEAD"))
    private void somethingsweaponry$onStopUsing(CallbackInfo ci) {
        if ((Object) this instanceof PlayerEntity) {
            this.somethingsweaponry$bladeBlockCount = 0;
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void somethingsweaponry$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof PlayerEntity player) {
            if (player.isUsingItem() && player.getActiveItem().getItem() instanceof BladeItem) {
                if (source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
                    return;
                }

                somethingsweaponry$bladeBlockCount++;

                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0f, 0.8f + player.getWorld().random.nextFloat() * 0.4f);

                if (somethingsweaponry$bladeBlockCount >= 3) {
                    player.getItemCooldownManager().set(player.getActiveItem().getItem(), 40);
                    player.clearActiveItem();
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 1.0f, 0.8f + player.getWorld().random.nextFloat() * 0.4f);
                    somethingsweaponry$bladeBlockCount = 0;
                }

                cir.setReturnValue(false);
            }
        }
    }

    //---------------------------
    // HeavyAxe Empowered attack
    //---------------------------
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float somethingsweaponry$applyHeavyAxeDamage(float amount, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if (source.getAttacker() instanceof PlayerEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();
            if (weapon.getItem() instanceof HeavyAxeItem heavyAxe) {
                NbtComponent customData = weapon.get(DataComponentTypes.CUSTOM_DATA);
                if (customData != null && customData.getNbt().getBoolean("Empowered")) {
                    //100tick
                    attacker.getItemCooldownManager().set(heavyAxe, 100);
                    weapon.remove(DataComponentTypes.CUSTOM_DATA);
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
