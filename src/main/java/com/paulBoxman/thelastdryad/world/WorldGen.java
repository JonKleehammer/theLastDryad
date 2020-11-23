package com.paulBoxman.thelastdryad.world;

import com.paulBoxman.thelastdryad.TheLastDryad;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TheLastDryad.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGen {

  @SubscribeEvent
  public static void addFeaturesToBiomes(BiomeLoadingEvent event){
//    event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.TOP_LAYER_MODIFICATION).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.LAKES).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.LOCAL_MODIFICATIONS).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).clear();
//    event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).clear();
  }

  @SubscribeEvent
  public static void removeWorldGen(BiomeLoadingEvent event) {

//    for (Biome biome : ForgeRegistries.BIOMES) {
//      BlockState top = biome.getGenerationSettings().getSurfaceBuilderConfig()
//    }

//    event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
//            Feature.ORE.withConfiguration(
//                    new OreFeatureConfig(
////                            OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
//                            new OreFeatureConfig.FillerBlockType(
//                                    RuleTest.
//                            ),
//                            Blocks.GLOWSTONE.getDefaultState(), 100
//                    )
//            ).withPlacement(Placement.RANGE.configure(
//                    new TopSolidRangeConfig(0, 5, 60)
//            )).square().func_242731_b(5)
//    );

  }
}
