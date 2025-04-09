package tf.ssf.sfort.invisframes;

import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;

public class InvisFramesShared {
	public static boolean IsEmpty(ArmorStandEntity entity) {
		for (ItemStack item : entity.getArmorItems())
			if (!item.isEmpty())
				return false;
		return true;
	}
	public static boolean IsEmpty(ArmorStandEntityRenderState entity) {
		if (!entity.equippedChestStack.isEmpty()) return false;
		if (!entity.equippedHeadStack.isEmpty()) return false;
		if (!entity.equippedLegsStack.isEmpty()) return false;
		if (!entity.equippedFeetStack.isEmpty()) return false;
		if (!entity.rightHandItemState.isEmpty()) return false;
		if (!entity.leftHandItemState.isEmpty()) return false;
		return true;
	}
}
