package vao211.somethingsweaponry.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vao211.somethingsweaponry.item.custom.HalberdItem;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class HalberdBodySpinMixin {

    @Inject(method = "setupTransforms*", at = @At("TAIL"))
    private void somethingsweaponry$spinPlayerBody(AbstractClientPlayerEntity player, MatrixStack matrices, float f, float g, float h, float i, CallbackInfo ci) {
        ItemStack mainHand = player.getMainHandStack();

        if (mainHand.getItem() instanceof HalberdItem) {
            float cooldown = player.getItemCooldownManager().getCooldownProgress(mainHand.getItem(), 0.0f);

            // Mốc 0.94f tương đương chuẩn 10 ticks (0.5 giây) trên tổng 160 ticks hồi chiêu
            if (cooldown > 0.94f) {
                float spinProgress = (cooldown - 0.94f) / 0.06f;

                // Xoay cơ thể 360 độ theo nhịp 0.5 giây vừa vặn
                float angle = (1.0f - spinProgress) * 360.0f;
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
            }
        }
    }
}