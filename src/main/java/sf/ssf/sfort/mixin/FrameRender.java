package sf.ssf.sfort.mixin;

import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sf.ssf.sfort.FrameConf;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class FrameRender {
	@Redirect(method = "render",at =@At(value = "INVOKE",target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;isInvisible()Z",ordinal = 0))
	public boolean itemBeInvis(ItemFrameEntity itemFrameEntity) {
		return itemFrameEntity.isInvisible()&&!itemFrameEntity.getHeldItemStack().isEmpty() || FrameConf.clientForceInvis;
	}
}
