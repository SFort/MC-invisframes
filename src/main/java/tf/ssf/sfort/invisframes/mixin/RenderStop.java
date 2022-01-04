package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.ssf.sfort.invisframes.RenderStopper;

@Mixin(value = BlockModelRenderer.class, priority = 2111)
public class RenderStop implements RenderStopper {
    boolean invisframes$stop = false;

    @Override
    public void stop(boolean bool) {
        invisframes$stop = bool;
    }

    @Inject(method="renderModelBrightnessColor(Lnet/minecraft/client/renderer/block/model/IBakedModel;FFFF)V", at=@At("HEAD"), cancellable=true)
    public void stopped(IBakedModel bakedModel, float p_178262_2_, float red, float green, float blue, CallbackInfo ci) {
        if(invisframes$stop) ci.cancel();
    }
}
