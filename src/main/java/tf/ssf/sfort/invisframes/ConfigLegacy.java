package tf.ssf.sfort.invisframes;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import tf.ssf.sfort.ini.SFIni;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLegacy {
	public static void loadLegacy(SFIni inIni) {
		Map<String, String> oldConf = new HashMap<>();
		File confFile = new File(
				FabricLoader.getInstance().getConfigDir().toString(),
				"InvisFrames.conf"
		);
		if (!confFile.exists()) return;
		try {
			List<String> la = Files.readAllLines(confFile.toPath());
			String[] ls = la.toArray(new String[Math.max(la.size(), 18)|1]);

			try{
				oldConf.put("frame.clientForceInvis", Boolean.toString(ls[0].contains("true")));
			}catch (Exception ignore){}
			try{
				oldConf.put("frame.allowProjectile", Boolean.toString(ls[2].contains("true")));
			}catch (Exception ignore){}
			try{
				oldConf.put("frame.itemCheck", ls[4].contains("both")? "both" : ls[4].contains("server")? "server" : ls[4].contains("none")? "none" : "client");
			}catch (Exception ignore){}
			try {
				oldConf.put("frame.clientHideCustomName", ls[6].contains("always") ? "always" : ls[6].contains("invis_only") ? "invis_only" : "never");
			} catch (Exception ignore) {}
			try{
				oldConf.put("frame.equipSound", Boolean.toString(!ls[8].contains("false")));
			}catch (Exception ignore){}
			try{
				oldConf.put("frame.toggleable", Boolean.toString(!ls[10].contains("false")));
			}catch (Exception ignore){}
			try{
				oldConf.put("stand.toggleable", Boolean.toString(!ls[12].contains("false")));
			}catch (Exception ignore){}
			try{
				oldConf.put("stand.allowProjectile", Boolean.toString(ls[14].contains("true")));
			}catch (Exception ignore){}
			try{
				oldConf.put("stand.itemCheck", ls[16].contains("both")? "both" : ls[16].contains("server")? "server" : ls[16].contains("none")? "none" : "client");
			}catch (Exception ignore){}
			for (Map.Entry<String, String> entry : oldConf.entrySet()) {
				SFIni.Data data = inIni.getLastData(entry.getKey());
				if (data != null) {
					data.val = entry.getValue();
				}
			}

			Files.delete(confFile.toPath());
			Config.LOGGER.log(Level.INFO,"tf.ssf.sfort.invisframes successfully loaded legacy config file");
		} catch(Exception e) {
			Config.LOGGER.log(Level.ERROR,"tf.ssf.sfort.invisframes failed to load legacy config file\n"+e);
		}
	}
}
