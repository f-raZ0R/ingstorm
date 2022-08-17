package net.fraZ0R.ingstorm;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ingstorm implements ModInitializer {
	public static final String modid = "ingstorm";
	public static final Logger LOGGER = LoggerFactory.getLogger(modid);
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing "+modid);
	}
}
