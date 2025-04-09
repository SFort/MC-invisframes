package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class FrameRenderForce {
	@Redirect(method = "render",at =@At(value = "FIELD",target = "Lnet/minecraft/client/render/entity/state/ItemFrameEntityRenderState;invisible:Z",ordinal = 0))
	public boolean itemBeInvis(ItemFrameEntityRenderState itemFrameEntityRenderState) {
		return !itemFrameEntityRenderState.itemRenderState.isEmpty();
	}
}
