package sf.ssf.sfort.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sf.ssf.sfort.FrameConf;

@Mixin(ItemFrameEntity.class)
public abstract class FrameEntity extends AbstractDecorationEntity {
	protected FrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "damage",at =@At(value = "INVOKE"),cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getAttacker();
		if (attacker !=null && attacker.isSneaky() && (FrameConf.allowProjectile || source.getName().equals("player"))) {
			this.setInvisible(!this.isInvisible());
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
