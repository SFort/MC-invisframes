package sf.ssf.sfort.invisframes.mixin;


import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.entity.item.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameRenderer.class)
public abstract class FrameRender {
	@Redirect(method = "render",at =@At(value = "INVOKE",target = "Lnet/minecraft/entity/item/ItemFrameEntity;isInvisible()Z",ordinal = 0))
	public boolean itemBeInvis(ItemFrameEntity itemFrameEntity) {
		return itemFrameEntity.isInvisible()&&!itemFrameEntity.getDisplayedItem().isEmpty();
	}
}
