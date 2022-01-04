package tf.ssf.sfort.invisframes.mixin;

import com.mumfrey.liteloader.core.LiteLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static tf.ssf.sfort.invisframes.Vals.*;

public class FrameConf implements IMixinConfigPlugin {
    public static final String mixin = "tf.ssf.sfort.invisframes.mixin";
    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onLoad(String mixinPackage) {
        // Configs
        File confFile = new File(
                LiteLoader.getConfigFolder().toString(),
                "InvisFrames.conf"
        );
        try {
            confFile.createNewFile();
            List<String> la = Files.readAllLines(confFile.toPath());
            List<String> defaultDesc = Arrays.asList(
                    "^-Should client always make frames with items invisible [false] true | false   // You would want to use this if the mod is installed client side only",
                    "^-Allow the use of projectiles for changing visibility [false] true | false //snowballs arrows etc",
                    "^-Frame has item check [client] client | server | both | none"
            );
            String[] ls = la.toArray(new String[Math.max(la.size(), defaultDesc.size() * 2)|1]);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{clientForceInvis = ls[0].contains("true");}catch (Exception ignore){}
            ls[0] = String.valueOf(clientForceInvis);
            try{allowProjectile = ls[2].contains("true");}catch (Exception ignore){}
            ls[2] = String.valueOf(allowProjectile);
            try{
                itemCheck = (ls[4].contains("both")? 3 : ls[4].contains("server")? 2 : ls[4].contains("none")? 0 : 1);}catch (Exception ignore){}
            ls[4] = itemCheck == 3 ? "both" : itemCheck == 2 ? "server": itemCheck == 0 ? "none" : "client";

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.invisframes successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load config file, using defaults\n"+e);
        }
    }


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals(mixin + ".FrameEntity")) return (itemCheck & 2) == 0;
        else if (mixinClassName.equals(mixin + ".FrameEntityServer"))  return (itemCheck & 2) != 0;
        else if (mixinClassName.equals(mixin + ".FrameRender")) return (itemCheck & 1) != 0;
        return true;
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }

    @Override
    public void preApply(java.lang.String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, java.lang.String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(java.lang.String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, java.lang.String mixinClassName, IMixinInfo mixinInfo) {

    }
}