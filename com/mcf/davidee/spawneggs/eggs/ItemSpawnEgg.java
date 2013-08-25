package com.mcf.davidee.spawneggs.eggs;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.mcf.davidee.spawneggs.DefaultFunctionality;
import com.mcf.davidee.spawneggs.SpawnEggRegistry;
import com.mcf.davidee.spawneggs.SpawnEggsMod;

public class ItemSpawnEgg extends Item {

	private Icon icon;

	public ItemSpawnEgg(int id) {
		super(id);
		setHasSubtypes(true);
		setCreativeTab(SpawnEggsMod.tabSpawnEggs);
		setUnlocalizedName("monsterPlacer");
	}

	public String getItemDisplayName(ItemStack stack) {
		String name = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
		SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

		if (info == null)
			return name;

		String mobID = info.mobID;
		String displayName = info.displayName;

		if (stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			if (compound.hasKey("mobID"))
				mobID = compound.getString("mobID");
			if (compound.hasKey("displayName"))
				displayName = compound.getString("displayName");
		}

		if (displayName == null)
			name += ' ' + attemptToTranslate("entity." + mobID + ".name", mobID);
		else 
			name += ' ' + attemptToTranslate("eggdisplay." + displayName, displayName);

		return name;
	}

	public int getColorFromItemStack(ItemStack stack, int par2) {
		SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

		if (info == null)
			return 16777215;

		int color = (par2 == 0) ? info.primaryColor : info.secondaryColor;

		if (stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			if (par2 == 0 && compound.hasKey("primaryColor"))
				color = compound.getInteger("primaryColor");
			if (par2 != 0 && compound.hasKey("secondaryColor"))
				color = compound.getInteger("secondaryColor");
		}

		return color;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
		if (world.isRemote) 
			return true;

		int i1 = world.getBlockId(x, y, z);
		x += Facing.offsetsXForSide[par7];
		y += Facing.offsetsYForSide[par7];
		z += Facing.offsetsZForSide[par7];
		double d0 = 0.0D;

		if (par7 == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
			d0 = 0.5D;

		Entity entity = spawnCreature(world, stack, x + 0.5D, y + d0, z + 0.5D);

		if (entity != null) {
			if (entity instanceof EntityLiving && stack.hasDisplayName())
				((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
			if (!player.capabilities.isCreativeMode)
				--stack.stackSize;
		}
		return true;

	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote)
			return stack;

		MovingObjectPosition trace = getMovingObjectPositionFromPlayer(world, player, true);

		if (trace == null)
			return stack;

		if (trace.typeOfHit == EnumMovingObjectType.TILE) {
			int x = trace.blockX;
			int y = trace.blockY;
			int z = trace.blockZ;

			if (!world.canMineBlock(player, x, y, z) || !player.canPlayerEdit(x, y, z, trace.sideHit, stack))
				return stack;

			if (world.getBlockMaterial(x, y, z) == Material.water) {
				
				Entity entity = spawnCreature(world, stack, x, y, z);
				if (entity != null) {
					if (entity instanceof EntityLiving && stack.hasDisplayName())
						((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
					if (!player.capabilities.isCreativeMode)
						--stack.stackSize;
				}
			}
		}

		return stack;
	}

	public static Entity spawnCreature(World world, ItemStack stack, double x, double y, double z) {
		SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

		if (info == null)
			return null;

		String mobID = info.mobID;
		NBTTagCompound spawnData = info.spawnData;

		if (stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			if (compound.hasKey("mobID"))
				mobID = compound.getString("mobID");
			if (compound.hasKey("spawnData"))
				spawnData = compound.getCompoundTag("spawnData");
		}

		Entity entity = null;

		entity = EntityList.createEntityByName(mobID, world);

		if (entity != null) {
			if (entity instanceof EntityLiving) {
				EntityLiving entityliving = (EntityLiving)entity;
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.func_110161_a((EntityLivingData)null);
				if (!spawnData.hasNoTags())
					addNBTData(entity, spawnData);
				world.spawnEntityInWorld(entity);
				entityliving.playLivingSound();
				spawnRiddenCreatures(entity, world, spawnData);
			}
		}

		return entity;
	}
	
	private static void spawnRiddenCreatures(Entity entity, World world, NBTTagCompound cur) {
		while (cur.hasKey("Riding")) {
		    cur = cur.getCompoundTag("Riding");
		    Entity newEntity = EntityList.createEntityByName(cur.getString("id"), world);
		    if (newEntity != null) {
		    	addNBTData(newEntity, cur);
		    	newEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		    	world.spawnEntityInWorld(newEntity);
		    	entity.mountEntity(newEntity);
		    }
		    entity = newEntity;
		}
	}

	private static void addNBTData(Entity entity, NBTTagCompound spawnData) {
		NBTTagCompound newTag = new NBTTagCompound();
		entity.addEntityID(newTag);

		for (NBTBase nbt : (Collection<NBTBase>) spawnData.getTags()) 
			newTag.setTag(nbt.getName(), nbt.copy());

		entity.readFromNBT(newTag);
	}


	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 > 0 ? icon : super.getIconFromDamageForRenderPass(par1, par2);
	}

	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list) {
		for (SpawnEggInfo info : SpawnEggRegistry.getEggInfoList()) 
			list.add(new ItemStack(par1, 1, info.eggID));
	}

	public void registerIcons(IconRegister iconRegister){
		itemIcon = iconRegister.registerIcon("spawn_egg");
		icon = iconRegister.registerIcon("spawn_egg_overlay");
	}

	public static String attemptToTranslate(String key, String _default) {
		String result = StatCollector.translateToLocal(key);
		return (result.equals(key)) ? _default : result;
	}
}
