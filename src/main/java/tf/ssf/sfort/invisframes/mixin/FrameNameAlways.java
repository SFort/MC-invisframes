package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class FrameNameAlways {
	@Inject(method="hasLabel(Lnet/minecraft/entity/decoration/ItemFrameEntity;)Z",at=@At("HEAD"), cancellable = true)
	public void itemHideName(ItemFrameEntity itemFrameEntity, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
		cir.cancel();
	}
}
