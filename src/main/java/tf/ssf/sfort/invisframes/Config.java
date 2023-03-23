package tf.ssf.sfort.invisframes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	public static Logger LOGGER = LogManager.getLogger();
	public enum ItemCheck {
		CLIENT(true, false),
		SERVER(false, true),
		BOTH(true, true),
		NONE(false, false);
		public final boolean client;
		public final boolean server;
		ItemCheck(boolean client, boolean server) {
			this.client = client;
			this.server = server;
		}
	}
	public enum NameCheck {
		NEVER, ALWAYS, INVIS_ONLY
	}
}
