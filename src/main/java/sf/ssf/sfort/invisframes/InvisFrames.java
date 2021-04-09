package sf.ssf.sfort.invisframes;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(InvisFrames.MODID)
public class InvisFrames {
    public static final String MODID = "invisframes";


    public InvisFrames() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
