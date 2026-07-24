package vao211.somethingsweaponry.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vao211.somethingsweaponry.item.custom.HalberdItem;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class HalberdLimbPoseMixin<T extends LivingEntity> {

    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;

    @Inject(method = "setAngles*", at = @At("TAIL"))
    private void somethingsweaponry$dynamicSweepPose(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity instanceof AbstractClientPlayerEntity player) {
            ItemStack mainHand = player.getMainHandStack();

            if (mainHand.getItem() instanceof HalberdItem) {
                float cooldown = player.getItemCooldownManager().getCooldownProgress(mainHand.getItem(), 0.0f);

                // Khớp mốc 0.94f (10 ticks) với cơ thể
                if (cooldown > 0.94f) {
                    float swingProgress = (1.0f - ((cooldown - 0.94f) / 0.06f));

                    // 1. XOẮN NẮM TAY (WRIST ROLL) - TRỊ TRIỆT ĐỂ BỆNH "CẦM CỜ"
                    // Xoắn nghiêng cánh tay từ phải (+0.6 rad) sang trái (-0.6 rad) trong lúc chém.
                    // Hành động này bẻ cây Halberd ngả xéo xuống song song với hướng chém ngang!
                    this.rightArm.roll = 0.6f - (swingProgress * 1.2f);
                    this.leftArm.roll = this.rightArm.roll - 0.2f;

                    // 2. NHÚN VAI THEO VÒNG CUNG (DYNAMIC PITCH ARC)
                    // Thay vì giữ đơ ở -1.4, tay sẽ chém từ trên vai phải xuống thắt lưng ở giữa đòn, rồi nhấc lên
                    float dipArc = (float) Math.sin(swingProgress * Math.PI) * 0.4f;
                    this.rightArm.pitch = -1.0f - dipArc;
                    this.leftArm.pitch = -0.9f - dipArc; // Tay trái bám sát tay phải giữ cán

                    // 3. VUNG QUÉT NGANG SIÊU RỘNG (WIDE YAW SLASH)
                    // Quét cánh tay bạt từ phía sau bên phải (-1.2 rad) vượt hẳn sang bên trái (+1.0 rad)
                    float horizontalSlash = -1.2f + (swingProgress * 2.2f);
                    this.rightArm.yaw = horizontalSlash;
                    this.leftArm.yaw = horizontalSlash + 0.35f; // Tay trái vươn qua ngực nắm lấy cán
                }
            }
        }
    }
}