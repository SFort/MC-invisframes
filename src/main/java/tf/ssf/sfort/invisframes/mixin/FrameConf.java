package tf.ssf.sfort.invisframes.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class FrameConf implements IMixinConfigPlugin {
    public static final String mixin = "tf.ssf.sfort.invisframes.mixin";
    public static Logger LOGGER = LogManager.getLogger();

    public static boolean clientForceInvis = false;
    public static boolean allowFrameProjectile = false;
    public static boolean allowStandProjectile = false;
    public static boolean playFrameSound = true;
    public static boolean enableInvisFrames = true;
    public static boolean enableInvisStands = true;
    //byte0-client, byte1-server
    public static int frameItemCheck = 1;
    public static int standItemCheck = 1;
    public static int clientHideFrameNames = 0;

    @Override
    public void onLoad(String mixinPackage) {
        // Configs
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "InvisFrames.conf"
        );
        try {
            confFile.createNewFile();
            List<String> la = Files.readAllLines(confFile.toPath());
            List<String> defaultDesc = Arrays.asList(
                    "^-Should client always make frames with items invisible [false] true | false   // You would want to use this if the mod is installed client side only",
                    "^-Allow the use of projectiles for changing frame visibility [false] true | false //snowballs arrows etc",
                    "^-Frame has item check [client] client | server | both | none",
                    "^-Frame hides item names [never] never | always | invis_only // client-side only",
                    "^-Frame plays equip sound on being toggled [true] true | false",
                    "^-Enable invis item frames [true] true | false",
                    "^-Enable invis armor stands [true] true | false",
                    "^-Allow the use of projectiles for changing armor stand visibility [false] true | false",
                    "^-Armor Stand has item check [client] client | server | both | none"
            );
            String[] ls = la.toArray(new String[Math.max(la.size(), defaultDesc.size() * 2)|1]);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{clientForceInvis = ls[0].contains("true");}catch (Exception ignore){}
            ls[0] = String.valueOf(clientForceInvis);
            try{
                allowFrameProjectile = ls[2].contains("true");}catch (Exception ignore){}
            ls[2] = String.valueOf(allowFrameProjectile);
            try{
                frameItemCheck = (ls[4].contains("both")? 3 : ls[4].contains("server")? 2 : ls[4].contains("none")? 0 : 1);}catch (Exception ignore){}
            ls[4] = frameItemCheck == 3 ? "both" : frameItemCheck == 2 ? "server": frameItemCheck == 0 ? "none" : "client";
            try {
                clientHideFrameNames = ls[6].contains("always") ? 1 : ls[6].contains("invis_only") ? 2 : 0;
            } catch (Exception ignore) {}
            ls[6] = clientHideFrameNames == 2 ? "invis_only" : clientHideFrameNames == 1 ? "always" : "never";
            try{playFrameSound = !ls[8].contains("false");}catch (Exception ignore){}
            ls[8] = String.valueOf(playFrameSound);
            try{enableInvisFrames = !ls[10].contains("false");}catch (Exception ignore){}
            ls[10] = String.valueOf(enableInvisFrames);
            try{enableInvisStands = !ls[12].contains("false");}catch (Exception ignore){}
            ls[12] = String.valueOf(enableInvisStands);
            try{allowStandProjectile = ls[14].contains("true");}catch (Exception ignore){}
            ls[14] = String.valueOf(allowStandProjectile);
            try{
                standItemCheck = (ls[16].contains("both")? 3 : ls[16].contains("server")? 2 : ls[16].contains("none")? 0 : 1);}catch (Exception ignore){}
            ls[16] = standItemCheck == 3 ? "both" : standItemCheck == 2 ? "server": standItemCheck == 0 ? "none" : "client";

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.invisframes successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load config file, using defaults\n"+e);
        }
    }


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            case mixin + ".FrameEntity" -> enableInvisFrames && (frameItemCheck & 2) == 0;
            case mixin + ".FrameEntityServer" -> enableInvisFrames && (frameItemCheck & 2) != 0;
            case mixin + ".FrameRender" -> (frameItemCheck & 1) != 0 && !clientForceInvis;
            case mixin + ".FrameRenderForce" -> (frameItemCheck & 1) != 0 && clientForceInvis;
            case mixin + ".FrameName" -> clientHideFrameNames == 2;
            case mixin + ".FrameNameAlways" -> clientHideFrameNames == 1;
            case mixin + ".StandRender" -> (standItemCheck & 1) != 0;
            case mixin + ".StandEntity" -> enableInvisStands && (standItemCheck & 2) == 0;
            case mixin + ".StandEntityServer" -> enableInvisStands && (standItemCheck & 2) != 0;
            default -> true;
        };
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    public static boolean IsEmpty(ArmorStandEntity entity) {
        for(ItemStack item : entity.getArmorItems())
            if(!item.isEmpty())
                return false;
        return true;
    }
}