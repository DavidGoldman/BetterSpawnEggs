package com.mcf.davidee.spawneggs;

import net.minecraftforge.common.Configuration;

public class SEConfig {
	
	public static int spawnEggID = 5731;
	
	public static void loadConfig(Configuration config){
		config.load();
		spawnEggID = config.getItem("spawnEggID", spawnEggID).getInt();
		config.save();
	}
	
}
