package tf.ssf.sfort.invisframes;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;

public class InvisFramesShared {
	public static boolean IsEmpty(ArmorStandEntity entity) {
		for (ItemStack item : entity.getArmorItems())
			if (!item.isEmpty())
				return false;
		return true;
	}
}
