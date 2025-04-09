package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class FrameName {
	@Inject(method="hasLabel",at=@At("HEAD"), cancellable = true)
	public void itemHideName(ItemFrameEntity itemFrameEntity, boolean b, CallbackInfoReturnable<Boolean> cir) {
		if (itemFrameEntity.isInvisible()&&!itemFrameEntity.getHeldItemStack().isEmpty()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
