package com.paulBoxman.thelastdryad.events;

import com.paulBoxman.thelastdryad.TheLastDryad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = TheLastDryad.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModClientEvents {

  @SubscribeEvent
  public static void sunlightHunger(LivingEvent.LivingUpdateEvent event) {

    LivingEntity entity = event.getEntityLiving();

    if (entity.getType() != EntityType.PLAYER){ return; }

    PlayerEntity player = (PlayerEntity) entity;
    World world = entity.getEntityWorld();


    BlockPos entityBlockPos = entity.getPosition();
    FoodStats food = player.getFoodStats();
    BlockState belowBlock = world.getBlockState(entityBlockPos.add(0, -1, 0));

    // if the player can see the sky fill up saturation
    if (world.canBlockSeeSky(entityBlockPos) || belowBlock.getBlock() == Blocks.SHROOMLIGHT) {
      food.setFoodSaturationLevel(20);
      return;
    }
    else {
      food.addExhaustion(0.004f);
    }
  }

  @SubscribeEvent
  public static void cancelEat(LivingEntityUseItemEvent event) {

    LivingEntity entity = event.getEntityLiving();

    if (entity.getType() != EntityType.PLAYER){ return; }
    if (!event.isCancelable()){ return; }
    if (!event.getItem().isFood()) { return; }

    event.setCanceled(true);
  }

  @SubscribeEvent
  public static void noGrassGeneration(ChunkEvent.Load event) {
    IChunk loadingChunk = event.getChunk();


//    Heightmap heightmap = loadingChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE);

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 0; y < loadingChunk.getHeight(); y++) {
          BlockPos newBlockPos = new BlockPos(x, y, z);
          Block targetBlock = loadingChunk.getBlockState(newBlockPos).getBlock();
          if (targetBlock == Blocks.GRASS_BLOCK) {
            loadingChunk.setBlockState(newBlockPos, Blocks.DIRT.getDefaultState(), false);
          }
        }
      }
    }
  }

  @SubscribeEvent
  public static void noFoliage(BiomeLoadingEvent event) {
    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).clear();
  }
}
