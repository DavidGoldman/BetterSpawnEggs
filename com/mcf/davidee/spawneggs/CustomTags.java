package com.mcf.davidee.spawneggs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CustomTags {
	
	public static NBTTagCompound poweredCreeper() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("powered", (byte) 1);
		return tag;
	}
	
	public static NBTTagCompound witherSkeleton() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("SkeletonType", (byte) 1);
		NBTTagList list = new NBTTagList();
		NBTTagCompound swordItem = createItemTag((byte) 1, (short) 0, (short) 272);
		list.appendTag(swordItem);
		for (int i = 0; i < 4; ++i)
			list.appendTag(new NBTTagCompound());
		tag.setTag("Equipment", list);
		return tag;
	}
	
	public static NBTTagCompound villagerZombie() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("IsVillager", (byte)1);
		return tag;
	}
	
	public static NBTTagCompound babyZombie() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("IsBaby", (byte)1);
		return tag;
	}
	
	public static NBTTagCompound horseType(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Type", type);
		return tag;
	}
	
	public static NBTTagCompound createItemTag(byte count, short damage, short id) {
		NBTTagCompound item = new NBTTagCompound();
		item.setByte("Count", count);
		item.setShort("Damage", damage);
		item.setShort("id", id);
		return item;
	}
	
	public static NBTTagCompound getEntityTag(String entityID) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("id", entityID);
		return tag;
	}
	
	public static NBTTagCompound ridingTag(NBTTagCompound ridden) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("Riding", ridden);
		return tag;
	}
	
	public static NBTTagCompound spiderJockey(boolean wither) {
		NBTTagCompound skele = (wither) ? witherSkeleton() : new NBTTagCompound();
		skele.setTag("Riding", getEntityTag("Spider"));
		return skele;
	}
	
	public static NBTTagCompound chickenJockey(boolean villager) {
		NBTTagCompound zomb = babyZombie();
		if (villager)
			zomb.setByte("IsVillager", (byte)1);
		zomb.setTag("Riding", getEntityTag("Chicken"));
		return zomb;
	}
}
