package tf.ssf.sfort.invisframes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.ssf.sfort.invisframes.Vals;


@Mixin(value = EntityItemFrame.class, priority = 2111)
public abstract class FrameEntityServer extends EntityHanging {

	public FrameEntityServer(World worldIn) {
		super(worldIn);
	}

	@Inject(method = "attackEntityFrom",at =@At("HEAD"),cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity attacker = source.getImmediateSource();
		if (attacker !=null && attacker.isSneaking() && (Vals.allowProjectile || attacker instanceof EntityPlayer)) {
			this.setInvisible(!(this.isInvisible() || ((EntityItemFrame)(Object)this).getDisplayedItem().isEmpty()));
			info.setReturnValue(true);
		}else
			this.setInvisible(false);
	}
	@Inject(method = "writeEntityToNBT", at = @At("HEAD"))
	public void writeEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
		compound.setBoolean("invisframes$invis", this.isInvisible());
	}

	@Inject(method = "readEntityFromNBT", at = @At("HEAD"))
	public void readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
		if (compound.getBoolean("invisframes$invis") == true) this.setInvisible(true);
	}
}
