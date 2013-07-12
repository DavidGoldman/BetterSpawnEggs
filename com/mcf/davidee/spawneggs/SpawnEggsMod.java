package com.mcf.davidee.spawneggs;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import com.mcf.davidee.spawneggs.eggs.DispenserBehaviorSpawnEgg;
import com.mcf.davidee.spawneggs.eggs.ItemSpawnEgg;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "SpawnEggs", name="Better Spawn Eggs",version="1.6.2.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class SpawnEggsMod {

	@Instance("SpawnEggs")
	private static SpawnEggsMod instance;

	public static CreativeTabs tabSpawnEggs = new CreativeTabs("spawnEggs") {
		public ItemStack getIconItemStack() {
			return new ItemStack(spawnEgg, 1, 0);
		}
	};

	public static ItemSpawnEgg spawnEgg;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SEConfig.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));

		spawnEgg = new ItemSpawnEgg(SEConfig.spawnEggID);

		GameRegistry.registerItem(spawnEgg, spawnEgg.getUnlocalizedName());
		BlockDispenser.dispenseBehaviorRegistry.putObject(spawnEgg, new DispenserBehaviorSpawnEgg());
		LanguageRegistry.instance().addStringLocalization("itemGroup.spawnEggs", "Spawn Eggs");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler 
	public void postInit(FMLPostInitializationEvent event) {
		DefaultFunctionality.addAllSpawnEggs();
	}
	
}
