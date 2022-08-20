package net.fraZ0R.ingstorm.common;

import eu.midnightdust.lib.config.MidnightConfig;

//ok so like I don't really know what I'm doing and I don't really want to mess with GSON so I'm just going to use
//a library mod.
public class IngstormConfig extends MidnightConfig{

    @MidnightConfig.Comment public static MidnightConfig.Comment restart;
    @Entry(min=0) public static float biomeDamage = 0.05f;
    @Entry(min=0) public static float blockDamage = 0.10f;


}
