package vao211.somethingsweaponry.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vao211.somethingsweaponry.item.custom.HeavyAxeItem;
import vao211.somethingsweaponry.item.custom.BattleHammerItem;
import vao211.somethingsweaponry.util.IEmpoweredEntity;

@Mixin(PlayerEntity.class)
public abstract class EmpoweredWeaponMixin implements IEmpoweredEntity {
    @Unique
    private int somethingsweaponry$empoweredTicks = 0;
    @Override
    public int somethingsweaponry$getEmpoweredTicks() { return this.somethingsweaponry$empoweredTicks; }
    @Override
    public void somethingsweaponry$setEmpoweredTicks(int ticks) { this.somethingsweaponry$empoweredTicks = ticks; }
    @Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
    private void somethingsweaponry$lockCooldownAtMax(float baseTime, CallbackInfoReturnable<Float> cir) {
        if (this.somethingsweaponry$empoweredTicks > 0) {
            cir.setReturnValue(1.0f);
        }
    }
    @Inject(method = "tick", at = @At("TAIL"))
    private void somethingsweaponry$tickEmpowered(CallbackInfo ci) {
        if (this.somethingsweaponry$empoweredTicks > 0) {
            this.somethingsweaponry$empoweredTicks--;
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (this.somethingsweaponry$empoweredTicks == 0 && !player.getWorld().isClient()) {
                Item heldItem = player.getMainHandStack().getItem();

                if (heldItem instanceof HeavyAxeItem || heldItem instanceof BattleHammerItem) {
                    player.getItemCooldownManager().set(heldItem, 100);
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5f, 1.0f);
                }
            }
        }
    }
}