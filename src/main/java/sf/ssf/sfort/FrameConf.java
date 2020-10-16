package sf.ssf.sfort;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class FrameConf implements ModInitializer{
    public static Logger LOGGER = LogManager.getLogger();

    public static boolean clientForceInvis = false;
    public static boolean allowProjectile = false;

    @Override
    public void onInitialize() {
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
                    "^-Allow the use of projectiles for changing visibility [false] true | false //snowballs arrows etc"
            );
            String[] ls = la.toArray(new String[Math.max(la.size(), defaultDesc.size() * 2)|1]);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{clientForceInvis = ls[0].contains("true");}catch (Exception ignore){}
            ls[0] = String.valueOf(clientForceInvis);
            try{allowProjectile = ls[2].contains("true");}catch (Exception ignore){}
            ls[2] = String.valueOf(allowProjectile);

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.invisframes successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load config file, using defaults\n"+e);
        }
    }
}