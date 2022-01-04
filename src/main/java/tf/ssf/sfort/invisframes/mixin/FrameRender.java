package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tf.ssf.sfort.invisframes.RenderStopper;
import tf.ssf.sfort.invisframes.Vals;

@Mixin(value = RenderItemFrame.class, priority = 2111)
public abstract class FrameRender {
    boolean invisframes$stop = false;

    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityItemFrame;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;getBlockModelShapes()Lnet/minecraft/client/renderer/BlockModelShapes;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void itemBeInvis(EntityItemFrame frame, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci, BlockPos pos, double d0, double d1, double d2, BlockRendererDispatcher render) {
        invisframes$stop = (Vals.clientForceInvis || frame.isInvisible())&&!frame.getDisplayedItem().isEmpty();
        ((RenderStopper)render.getBlockModelRenderer()).stop(invisframes$stop);
    }
    @ModifyConstant(method = "doRender(Lnet/minecraft/entity/item/EntityItemFrame;DDDFF)V", constant = @Constant(floatValue = 0.4375F))
    public float itemBeInvis2(float constant) {
        if(invisframes$stop) return 0.5F;
        return constant;
    }

}
