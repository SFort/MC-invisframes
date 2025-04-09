package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.ssf.sfort.invisframes.InvisFramesShared;

@Mixin(ArmorStandEntity.class)
public abstract class StandEntityServer extends LivingEntity {
	@Shadow public abstract void setInvisible(boolean invisible);

	protected StandEntityServer(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method="damage", at=@At("HEAD"), cancellable = true)
	public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getAttacker();
		if (attacker !=null && attacker.isSneaky() && (FrameConf.allowStandProjectile || source.getName().equals("player"))) {
			this.setInvisible(!(this.isInvisible() || InvisFramesShared.IsEmpty((ArmorStandEntity) (Object) this)));
			info.setReturnValue(true);
			info.cancel();
		} else {
			this.setInvisible(false);
		}
	}
	@Inject(method="equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", at=@At("RETURN"))
	public void update(CallbackInfo ci) {
		if (InvisFramesShared.IsEmpty((ArmorStandEntity) (Object) this)) {
			this.setInvisible(false);
		}
	}
}
