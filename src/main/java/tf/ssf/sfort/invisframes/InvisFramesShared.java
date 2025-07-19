package tf.ssf.sfort.invisframes;

import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;

public class InvisFramesShared {
	public static boolean IsEmpty(ArmorStandEntity entity) {
		if (entity.hasStackEquipped(EquipmentSlot.HEAD)) return false;
		if (entity.hasStackEquipped(EquipmentSlot.CHEST)) return false;
		if (entity.hasStackEquipped(EquipmentSlot.LEGS)) return false;
		if (entity.hasStackEquipped(EquipmentSlot.FEET)) return false;
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
