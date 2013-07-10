package com.mcf.davidee.spawneggs.eggs;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public final class DispenserBehaviorSpawnEgg extends BehaviorDefaultDispenseItem {
	
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
		EnumFacing enumfacing = BlockDispenser.getFacing(blockSource.getBlockMetadata());
		double x = blockSource.getX() + (double)enumfacing.getFrontOffsetX();
		double y = (double)((float)blockSource.getYInt() + 0.2F);
		double z = blockSource.getZ() + (double)enumfacing.getFrontOffsetZ();
		Entity entity = ItemSpawnEgg.spawnCreature(blockSource.getWorld(), stack, x, y, z);
		if (entity instanceof EntityLiving && stack.hasDisplayName())
			((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());

		stack.splitStack(1);
		
		return stack;
	}
}
