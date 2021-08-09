package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemFrameEntity.class, priority = 2111)
public abstract class FrameEntityServer extends AbstractDecorationEntity {
	protected FrameEntityServer(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow public abstract ItemStack getHeldItemStack();

	@Inject(method = "damage",at =@At("HEAD"),cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getAttacker();
		if (attacker !=null && attacker.isSneaky() && (FrameConf.allowProjectile || source.getName().equals("player"))) {
			this.setInvisible(!(this.isInvisible() || this.getHeldItemStack().isEmpty()));
			info.setReturnValue(true);
		}else
			this.setInvisible(false);
	}
}
