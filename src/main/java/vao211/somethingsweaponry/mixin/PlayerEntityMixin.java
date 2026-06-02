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
}
