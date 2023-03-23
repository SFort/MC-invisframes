package tf.ssf.sfort.invisframes.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import tf.ssf.sfort.ini.SFIni;
import tf.ssf.sfort.invisframes.Config;
import tf.ssf.sfort.invisframes.ConfigLegacy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

public class FrameConf implements IMixinConfigPlugin {
    public static final String mixin = "tf.ssf.sfort.invisframes.mixin";

    public static boolean clientForceInvis = false;
    public static boolean allowFrameProjectile = false;
    public static boolean allowStandProjectile = false;
    public static boolean playFrameSound = true;
    public static boolean enableInvisFrames = true;
    public static boolean enableInvisStands = false;
    public static Config.ItemCheck frameItemCheck = Config.ItemCheck.CLIENT;
    public static Config.ItemCheck standItemCheck = Config.ItemCheck.NONE;
    public static Config.NameCheck clientHideFrameNames = Config.NameCheck.NEVER;

    @Override
    public void onLoad(String mixinPackage) {
        loadConfig();
    }
    public void loadConfig() {
        SFIni defIni = new SFIni();
        defIni.load(String.join("\n", new String[]{
                "; Enable invisible item frame toggle [true] true | false",
                "frame.toggleable=true",
                "; Should client always make frames with items invisible [false] true | false",
                ";  You might want to use this if the mod is installed client side",
                "frame.clientForceInvis=false",
                "; Allow the use of projectiles for changing frame visibility [false] true | false",
                ";  For example shift throwing a snowball or firing an arrow",
                "frame.allowProjectile=false",
                "; Frame has item check [client] client | server | both | none",
                ";  the item check makes the frame visible when it contains an item",
                "frame.itemCheck=client",
                "; Client hide item frame item names [never] never | always | invis_only",
                "frame.clientHideCustomName=never",
                "; Frame plays equip sound on toggle [true] true | false",
                "frame.equipSound=true",
                "; Enable armor stand toggle [false] true | false",
                "stand.toggleable=false",
                "; Allow the use of projectiles for changing armor stand visibility [false] true | false",
                ";  For example shift throwing a snowball or firing an arrow",
                "stand.allowProjectile=false",
                "; Armor stand has item check [none] client | server | both | none",
                ";  the item check makes the frame visible when it contains an item",
                "stand.itemCheck=none",
        }));
        ConfigLegacy.loadLegacy(defIni);
        File confFile = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                "InvisFrames.sf.ini"
        );
        if (!confFile.exists()) {
            try {
                Files.write(confFile.toPath(), defIni.toString().getBytes());
                loadIni(defIni);
            } catch (IOException e) {
                Config.LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to create config file, using defaults", e);
            }
            return;
        }
        try {
            SFIni ini = new SFIni();
            String text = Files.readString(confFile.toPath());
            int hash = text.hashCode();
            ini.load(text);
            for (Map.Entry<String, List<SFIni.Data>> entry : defIni.data.entrySet()) {
                List<SFIni.Data> list = ini.data.get(entry.getKey());
                if (list == null || list.isEmpty()) {
                    ini.data.put(entry.getKey(), entry.getValue());
                } else {
                    list.get(0).comments = entry.getValue().get(0).comments;
                }
            }
            loadIni(ini);
            String iniStr = ini.toString();
            if (hash != iniStr.hashCode()) {
                Files.write(confFile.toPath(), iniStr.getBytes());
            }
        } catch(Exception e) {
            Config.LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load config file, using defaults\n"+e);
        }
    }
    public static<E extends Enum<E>> void setOrResetEnum(SFIni ini, String key, Consumer<E> set, E en, Class<E> clazz) {
        try {
            set.accept(ini.getEnum(key, clazz));
        } catch (Exception e) {
            SFIni.Data data = ini.getLastData(key);
            if (data != null) data.val = en.name().toLowerCase(Locale.ROOT);
            Config.LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load "+key+", setting to default value", e);
        }
    }
    public static void setOrResetBool(SFIni ini, String key, Consumer<Boolean> set, boolean bool) {
        try {
            set.accept(ini.getBoolean(key));
        } catch (Exception e) {
            SFIni.Data data = ini.getLastData(key);
            if (data != null) data.val = Boolean.toString(bool);
            Config.LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load "+key+", setting to default value", e);
        }
    }
    public static void loadIni(SFIni ini) {
        setOrResetBool(ini, "frame.toggleable", b->enableInvisFrames=b, enableInvisFrames);
        setOrResetBool(ini, "frame.clientForceInvis", b->clientForceInvis=b, clientForceInvis);
        setOrResetBool(ini, "frame.allowProjectile", b->allowFrameProjectile=b, allowFrameProjectile);
        setOrResetBool(ini, "frame.equipSound", b->playFrameSound=b, playFrameSound);
        setOrResetBool(ini, "stand.toggleable", b->enableInvisStands=b, enableInvisStands);
        setOrResetBool(ini, "stand.allowProjectile", b->allowStandProjectile=b, allowStandProjectile);

        setOrResetEnum(ini, "frame.clientHideCustomName", e->clientHideFrameNames=e, clientHideFrameNames, Config.NameCheck.class);
        setOrResetEnum(ini, "frame.itemCheck", e->frameItemCheck=e, frameItemCheck, Config.ItemCheck.class);
        setOrResetEnum(ini, "stand.itemCheck", e->standItemCheck=e, standItemCheck, Config.ItemCheck.class);
        Config.LOGGER.log(Level.INFO,"tf.ssf.sfort.invisframes successfully loaded config file");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            case mixin + ".FrameEntity" -> enableInvisFrames && !frameItemCheck.server;
            case mixin + ".FrameEntityServer" -> enableInvisFrames && frameItemCheck.server;
            case mixin + ".FrameRender" -> frameItemCheck.client && !clientForceInvis;
            case mixin + ".FrameRenderForce" -> frameItemCheck.client && clientForceInvis;
            case mixin + ".FrameName" -> clientHideFrameNames == Config.NameCheck.INVIS_ONLY;
            case mixin + ".FrameNameAlways" -> clientHideFrameNames == Config.NameCheck.ALWAYS;
            case mixin + ".StandRender" -> standItemCheck.client;
            case mixin + ".StandEntity" -> enableInvisStands && !standItemCheck.server;
            case mixin + ".StandEntityServer" -> enableInvisStands && standItemCheck.server;
            default -> true;
        };
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

}