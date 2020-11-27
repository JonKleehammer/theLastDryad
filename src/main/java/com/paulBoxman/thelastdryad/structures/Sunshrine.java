package com.paulBoxman.thelastdryad.structures;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Sunshrine {

  public static int checkSunshrine(World world, BlockPos belowBlockPos) {

    if (getBlock(world, belowBlockPos) != Blocks.GOLD_BLOCK) { return 0; }

    int[] bounds = getShrineBounds(world, belowBlockPos);
    if (bounds == null) { return 0; }


    if (checkShrineBounds(world, bounds, belowBlockPos.getY())){
      breakGlowstone(world, bounds, belowBlockPos.getY());
      return getGoldCount(bounds);
    }
    else {
      return 0;
    }

  }

  private static Block getBlock(World world, BlockPos pos) {
    return world.getBlockState(pos).getBlock();
  }

  private static int[] getShrineBounds(World world, BlockPos startPos) {
    int maxSize = 25;

    int xMin = startPos.getX();
    for (int i = 1; i < maxSize; i++) {
      BlockPos targetPos = startPos.add(-i, 0, 0);
      if (getBlock(world, targetPos) == Blocks.GLOWSTONE) {
        xMin = targetPos.getX();
        break;
      }
      else if (getBlock(world, targetPos) == Blocks.GOLD_BLOCK) {
      }
      else return null;
    }

    int xMax = startPos.getX();
    for (int i = 1; i < maxSize; i++) {
      BlockPos targetPos = startPos.add(i, 0, 0);
      if (getBlock(world, targetPos) == Blocks.GLOWSTONE) {
        xMax = targetPos.getX();
        break;
      }
      else if (getBlock(world, targetPos) == Blocks.GOLD_BLOCK) {
      }
      else return null;
    }

    int zMin = startPos.getZ();
    for (int i = 1; i < maxSize; i++) {
      BlockPos targetPos = startPos.add(0, 0, -i);
      if (getBlock(world, targetPos) == Blocks.GLOWSTONE) {
        zMin = targetPos.getZ();
        break;
      }
      else if (getBlock(world, targetPos) == Blocks.GOLD_BLOCK) {
      }
      else return null;
    }

    int zMax = startPos.getZ();
    for (int i = 1; i < maxSize; i++) {
      BlockPos targetPos = startPos.add(0, 0, i);
      if (getBlock(world, targetPos) == Blocks.GLOWSTONE) {
        zMax = targetPos.getZ();
        break;
      }
      else if (getBlock(world, targetPos) == Blocks.GOLD_BLOCK) {
      }
      else return null;
    }

    int width = xMax - xMin + 1;
    int length = zMax - zMin + 1;

    if (width > maxSize || length > maxSize) {
      return  null;
    }


    return new int[] {xMin, xMax, zMin, zMax};
  }

  private static boolean checkShrineBounds(World world, int[] shrineBounds, int yLevel) {

    int xMin = shrineBounds[0];
    int xMax = shrineBounds[1];
    int zMin = shrineBounds[2];
    int zMax = shrineBounds[3];



    for (int i = xMin; i <= xMax; i++) {
      for (int j = zMin; j <= zMax; j++) {
        BlockPos targetBlockPos = new BlockPos(i, yLevel, j);
        Block targetBlock = getBlock(world, targetBlockPos);

//        world.setBlockState(targetBlockPos, Blocks.REDSTONE_BLOCK.getDefaultState());

        if (i == xMin || i == xMax || j == zMin || j == zMax) {
          if (targetBlock != Blocks.GLOWSTONE) {
            return false;
          }
        }
        else if (targetBlock == Blocks.GOLD_BLOCK) {
        }
        else {
          return false;
        }
      }
    }

    // if we made it this far then it's a valid shrine
    return true;
  }

  private static void breakGlowstone(World world, int[] shrineBounds, int yLevel) {

    int xMin = shrineBounds[0];
    int xMax = shrineBounds[1];
    int zMin = shrineBounds[2];
    int zMax = shrineBounds[3];

    // maybe break all the glowstone?
    for (int i = xMin; i <= xMax; i++) {
      for (int j = zMin; j <= zMax; j++) {
        BlockPos targetBlockPos = new BlockPos(i, yLevel, j);
        Block targetBlock = getBlock(world, targetBlockPos);

        if (targetBlock == Blocks.GLOWSTONE) {
          world.destroyBlock(targetBlockPos, false);
        }
      }
    }
  }

  private static int getGoldCount(int[] shrineBounds) {

    int xMin = shrineBounds[0];
    int xMax = shrineBounds[1];
    int zMin = shrineBounds[2];
    int zMax = shrineBounds[3];

    int width = xMax - xMin + 1;
    int length = zMax - zMin + 1;

    return width * length;
  }

}
