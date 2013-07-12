package com.mcf.davidee.spawneggs;

import static com.mcf.davidee.spawneggs.CustomTags.babyZombie;
import static com.mcf.davidee.spawneggs.CustomTags.horseType;
import static com.mcf.davidee.spawneggs.CustomTags.poweredCreeper;
import static com.mcf.davidee.spawneggs.CustomTags.villagerZombie;
import static com.mcf.davidee.spawneggs.CustomTags.witherSkeleton;

import java.util.Collection;

import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

import com.mcf.davidee.spawneggs.eggs.SpawnEggInfo;

public class DefaultFunctionality {

	//+10,000 to all vanilla eggs
	//Please don't overflow (possible with mod's spawn eggs)
	public static final int VANILLA_START = 10000;

	private static void addVanillaSpawnEggs() throws RuntimeException {
		for (EntityEggInfo info : (Collection<EntityEggInfo>) EntityList.entityEggs.values()) {
			int registerID = VANILLA_START + info.spawnedID;
			String mobID = EntityList.getStringFromID(info.spawnedID);

			if (registerID > Short.MAX_VALUE || registerID < Short.MIN_VALUE)
				throw new RuntimeException("RegisterID out of bounds for " + mobID);
			
			try {
				SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)registerID, mobID, 
						new NBTTagCompound(), info.primaryColor, info.secondaryColor));
			}
			catch(IllegalArgumentException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void addAllSpawnEggs() throws RuntimeException {
		addVanillaSpawnEggs();
		try {
			//TODO Localize
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 0, "Pig", "Custom", new NBTTagCompound(), 
					0xFFFFFF, 0x0));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 1, "EntityHorse", "Donkey", horseType(1), 
					0x808080, 0xBD8B72));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 2, "EntityHorse", "Mule", horseType(2), 
					0x2B1D0E, 0x9A6448));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 3, "EntityHorse", "Undead Horse", horseType(3), 
					0x2200, 0x6600));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 4, "EntityHorse", "Skeleton Horse", horseType(4), 
					0xfffaf0, 0xcdb79e));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 7, "SnowMan", new NBTTagCompound(), 
					0xFFFFFF, 0xFF9000));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 8, "VillagerGolem", new NBTTagCompound(), 
					0xFFCC99, 0xFFFFFF));
			
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)10, "Creeper", "Powered Creeper", poweredCreeper(), 
					0xDA70B, 0xBBFF));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)11, "Skeleton", "Wither Skeleton", witherSkeleton(), 
					0xC1C1C1, 0x0));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)15, "Giant", "Giant Zombie", new NBTTagCompound(),
					0xAFAF, 0xAFAF));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)16, "Zombie", "Villager Zombie", villagerZombie(),
					0xAFAF, 0xBD8B72));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)17, "Zombie", "Baby Zombie", babyZombie(),
					0xAFAF, 0xFFFFFF));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)18, "PigZombie", "Baby Zombie Pigman", babyZombie(),
					0xEA9393, 0xFFFFFF));
			
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)20, "WitherBoss", new NBTTagCompound(),
					0x272727, 0x777777));
			SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)21, "EnderDragon", new NBTTagCompound(),
					0x0, 0x633188));

		}
		catch(IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

}
