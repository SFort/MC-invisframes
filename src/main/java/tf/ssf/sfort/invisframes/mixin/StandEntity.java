package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class StandEntity extends LivingEntity {
	protected StandEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method="damage", at=@At("HEAD"), cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getAttacker();
		if (attacker !=null && attacker.isSneaky() && (FrameConf.allowStandProjectile || source.getName().equals("player"))) {
			this.setInvisible(!this.isInvisible());
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
