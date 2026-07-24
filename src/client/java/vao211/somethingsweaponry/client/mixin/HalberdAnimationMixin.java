package vao211.somethingsweaponry.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vao211.somethingsweaponry.item.custom.HalberdItem;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public abstract class HalberdAnimationMixin {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void somethingsweaponry$renderHalberdSweep(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        // Kiểm tra đúng là Halberd
        if (item.getItem() instanceof HalberdItem) {

            float cooldown = player.getItemCooldownManager().getCooldownProgress(item.getItem(), tickDelta);

            // Halberd hồi chiêu 160 ticks (8 giây).
            // Ta cho hoạt ảnh chém quét diễn ra trong ~6 ticks đầu tiên (khi cooldown từ 1.0 tụt xuống 0.96)
            if (cooldown > 0.96f) {

                // Chuẩn hóa tiến trình từ 0.0 -> 1.0
                float sweepProgress = (cooldown - 0.96f) / 0.04f;

                // Hàm Sin tạo lực chém: Bắt đầu ở tay phải -> Vung mạnh sang trái -> Rút về
                float sweepAngle = (float) Math.sin(sweepProgress * Math.PI);

                // 1. CHÉM NGANG TRỤC Y (XOAY TỪ PHẢI SANG TRÁI)
                // Xoay vũ khí một góc 70 độ băng ngang qua màn hình
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sweepAngle * -70.0f));

                // 2. NGHIÊNG LƯỠI VŨ KHÍ TRỤC Z (TẠO DÁNG CHÉM BỔ CỦI / QUÉT FLAT)
                // Đè nghiêng cây rìu xuống 45 độ để lưỡi chém song song với mặt đất
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(sweepAngle * -45.0f));

                // 3. ĐẨY VŨ KHÍ RA XA MỘT CHÚT ĐỂ TẠO CẢM GIÁC VUNG VNG RỘNG
                matrices.translate(sweepAngle * -0.5f, 0.0, sweepAngle * -0.5f);
            }
        }
    }
}