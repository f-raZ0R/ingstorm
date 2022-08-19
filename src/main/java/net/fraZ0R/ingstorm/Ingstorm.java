package net.fraZ0R.ingstorm;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fraZ0R.ingstorm.common.IngstormConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ingstorm implements ModInitializer {
	public static final String modid = "ingstorm";
	public static final Logger LOGGER = LoggerFactory.getLogger(modid);
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing "+modid);
	}

	static {
		MidnightConfig.init(modid, IngstormConfig.class);
	}


}
