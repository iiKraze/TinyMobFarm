package cn.davidma.tinymobfarm.core.util;

import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EntityHelper {
	
	public static boolean isMobBlacklisted(LivingEntity livingEntity) {
		EntityType<?> entityType = livingEntity.getType();
		return isMobBlacklisted(entityType);
	}

	public static boolean isMobBlacklisted(EntityType<?> entityType) {
		String mobName = entityType.getRegistryName().toString();
		for (String i: Config.MOB_BLACKLIST) {
			if (mobName.equalsIgnoreCase(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getLootTableLocation(LivingEntity livingEntity) {
		return livingEntity.getLootTableResourceLocation().toString();
	}
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, World world, int loot) {
		LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
		LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
		LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
		FakePlayer daniel = FakePlayerHelper.getPlayer((ServerWorld) world);
		
		builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, daniel);
		
		LootParameterSet.Builder setBuilder = new LootParameterSet.Builder();
		setBuilder.required(LootParameters.LAST_DAMAGE_PLAYER);
		return lootTable.generate(builder.build(setBuilder.build()));
	}
}
