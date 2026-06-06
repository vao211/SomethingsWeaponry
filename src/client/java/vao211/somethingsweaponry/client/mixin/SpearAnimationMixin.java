package vao211.somethingsweaponry.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vao211.somethingsweaponry.item.custom.SpearItem;

@Mixin(HeldItemRenderer.class)
public abstract class SpearAnimationMixin {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void somethingsweaponry$renderSpearThrust(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (item.getItem() instanceof SpearItem) {

            float cooldown = player.getItemCooldownManager().getCooldownProgress(item.getItem(), tickDelta);

            float animationCutoff = 0.8f;

            if (cooldown > animationCutoff) {
                float thrustProgress = (cooldown - animationCutoff) / (1.0f - animationCutoff);
                float thrustDistance = (float) Math.sin(thrustProgress * Math.PI) * 1.1f;

                matrices.translate(0.0, 0.0, -thrustDistance);
            }
        }
    }
}