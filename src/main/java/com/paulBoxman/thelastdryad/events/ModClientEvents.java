package com.paulBoxman.thelastdryad.events;

import com.paulBoxman.thelastdryad.TheLastDryad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.List;
import java.util.Random;
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

//    player.sendMessage(ITextComponent.getTextComponentOrEmpty(Long.toString(world.getDayTime())), player.getUniqueID());

    BlockPos entityBlockPos = entity.getPosition();
    FoodStats food = player.getFoodStats();
    BlockState belowBlock = world.getBlockState(entityBlockPos.add(0, -1, 0));

    // if the player can see the sky fill up saturation
    if ((world.canBlockSeeSky(entityBlockPos) && world.getDayTime() % 24000 < 12750)
            || belowBlock.getBlock() == Blocks.SHROOMLIGHT) {
      if (food.getFoodLevel() < 60){
        food.setFoodLevel(food.getFoodLevel() + 1);
        if (food.getFoodLevel() == 58) {
          player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        }
      }
//      food.setFoodSaturationLevel(food.getFoodLevel());

      player.addPotionEffect(new EffectInstance(Effects.SPEED, 219, 1));
      player.addPotionEffect(new EffectInstance(Effects.HASTE, 219, 1));
    }
    else {
        food.addExhaustion(0.006f);
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

  private static final String NBT_KEY = TheLastDryad.MOD_ID + ".firstjoin";
  @SubscribeEvent
  public static void grassBlockOnJoin(PlayerEvent.PlayerLoggedInEvent event){

    PlayerEntity player = event.getPlayer();

    CompoundNBT data = player.getPersistentData();
    CompoundNBT persistent;
    if (!data.contains(player.PERSISTED_NBT_TAG)) {
      data.put(player.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
    }
    else {
      persistent = data.getCompound(player.PERSISTED_NBT_TAG);
    }

    if (!persistent.contains(NBT_KEY)) {
      persistent.putBoolean(NBT_KEY, true);
      player.addItemStackToInventory(new ItemStack(Blocks.GRASS_BLOCK));
    }
  }


  static Block hackBlock = Blocks.OBSIDIAN;
  @SubscribeEvent
  public static void noGrassGeneration(ChunkEvent.Load event) {

    IChunk loadingChunk = event.getChunk();

//    Heightmap heightmap = loadingChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE);

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 0; y < loadingChunk.getHeight(); y++) {

          BlockPos targetPos = new BlockPos(x, y, z);
          Block targetBlock = loadingChunk.getBlockState(targetPos).getBlock();

          if (x == 0 && y == 0 && z == 0) {
            if (targetBlock == hackBlock) {
              return;
            }
            else  {
              // First time this chunk has ever been loaded
              loadingChunk.setBlockState(targetPos, hackBlock.getDefaultState(), false);
            }
          }

          if (targetBlock == Blocks.GRASS_BLOCK) {
            loadingChunk.setBlockState(targetPos, Blocks.DIRT.getDefaultState(), false);
          }
        }
      }
    }
  }


  @SubscribeEvent
  public static void noFoliage(BiomeLoadingEvent event)
  {

    Biome.Category category = event.getCategory();
    String biomeName = category.getName();
    if (
      biomeName.equals("nether") ||
      biomeName.equals("the_end")
    ) {
      return;
    }

    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).clear();
  }

  @SubscribeEvent
  public static void removeHerbivores (LivingSpawnEvent event) {

    LivingEntity entity = event.getEntityLiving();
    IWorld world = event.getWorld();

    if (
            entity.getType() == EntityType.COW ||
            entity.getType() == EntityType.SHEEP ||
            entity.getType() == EntityType.PIG ||
            entity.getType() == EntityType.HORSE ||
            entity.getType() == EntityType.RABBIT ||
            entity.getType() == EntityType.CHICKEN ||
            entity.getType() == EntityType.DONKEY ||
            entity.getType() == EntityType.MULE ||
            entity.getType() == EntityType.LLAMA ||
            entity.getType() == EntityType.PANDA
    )
    {

      BlockPos entityPos = entity.getPosition();
      BlockState belowBlockState = world.getBlockState(entityPos.add(0, -1, 0));
      Block belowBlock = belowBlockState.getBlock();

      if (belowBlock != Blocks.GRASS_BLOCK) {
        entity.remove();
      }
    }
  }
}
