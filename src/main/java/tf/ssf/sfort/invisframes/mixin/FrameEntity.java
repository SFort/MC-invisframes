package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class FrameEntity extends AbstractDecorationEntity {
	protected FrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "damage",at =@At("HEAD"),cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getAttacker();
		if (attacker !=null && attacker.isSneaky() && (FrameConf.allowFrameProjectile || source.getName().equals("player"))) {
			World world = getWorld();
			if (FrameConf.playFrameSound && world instanceof ServerWorld) {
				world.playSound(null, getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), SoundCategory.PLAYERS, 1, .7f + random.nextFloat()*.3f);
			}
			this.setInvisible(!this.isInvisible());
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
