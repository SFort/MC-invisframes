package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class StandRender {
	private boolean survivalflight$secondBool = false;

	@ModifyVariable(method="getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;", at=@At("HEAD"), ordinal=0, argsOnly=true)
	public boolean getRenderLayer(boolean old, LivingEntity entity, boolean bl) {
		if (entity instanceof ArmorStandEntity) {
			if (FrameConf.IsEmpty((ArmorStandEntity) entity)) {
				survivalflight$secondBool = false;
				return true;
			} else {
				survivalflight$secondBool = !old;
			}
		}
		return old;
	}
	@ModifyVariable(method="getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;", at=@At("HEAD"), ordinal=1, argsOnly=true)
	public boolean getRenderLayer2(boolean old, LivingEntity entity, boolean bl) {
		if (survivalflight$secondBool) return false;
		return old;
	}

}
