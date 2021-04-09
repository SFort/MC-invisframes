package sf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class FrameEntity extends HangingEntity {
	protected FrameEntity(EntityType<? extends HangingEntity> type, World p_i48561_2_) {
		super(type, p_i48561_2_);
	}

	@Inject(method = "attackEntityFrom",at =@At(value = "INVOKE"),cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getTrueSource();
		if (attacker !=null && attacker.isSneaking()) {
			this.setInvisible(!this.isInvisible());
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
