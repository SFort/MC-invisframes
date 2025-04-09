package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tf.ssf.sfort.invisframes.InvisFramesShared;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class StandRender {
	private boolean survivalflight$secondBool = false;

	@ModifyVariable(method="getRenderLayer(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;", at=@At("HEAD"), ordinal=0, argsOnly=true)
	public boolean getRenderLayer(boolean old, ArmorStandEntityRenderState entity, boolean bl) {
		if (InvisFramesShared.IsEmpty(entity)) {
			survivalflight$secondBool = false;
			return true;
		} else {
			survivalflight$secondBool = !old;
		}
		return old;
	}
	@ModifyVariable(method="getRenderLayer(Lnet/minecraft/client/render/entity/state/ArmorStandEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;", at=@At("HEAD"), ordinal=1, argsOnly=true)
	public boolean getRenderLayer2(boolean old, ArmorStandEntityRenderState entity, boolean bl) {
		if (survivalflight$secondBool) return false;
		return old;
	}

}
