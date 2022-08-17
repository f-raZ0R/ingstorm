package net.fraZ0R.ingstorm.common;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockProximity {

    public static boolean isSafe(BlockPos pos, World world,Block block, int range)
    {
        /*ASSUMPTIONS MADE (THAT COMPAT WITH MODS THAT BREAK THESE ASSUMPTIONS WILL HAVE TO HANDLE):
        THE PLAYER IS 1X2x1. (supporting pehkui is a headache that is currently out of scope)
        BLOCKS ARE 1 BLOCK BIG.
        NO USER WOULD WANT TO SPECIFICALLY CHECK FOR A SPECIFIC BLOCKSTATE (ok this one is obviously wrong, but I don't want to deal with the headache yet)*/
        int blockX = pos.getX() - range;
        int blockY = pos.getY() - range;
        int blockZ = pos.getZ() - range;

        //hmm, today I will make a triply-nested for loop that is called every single tick :clueless:
        for(;blockX<=pos.getX()+range; blockX++)
        {
            for(;blockY<=pos.getY()+range+1; blockY++)
            {
                for(;blockZ<=pos.getZ()+range;blockZ++)
                {
                    BlockPos checkPos = new BlockPos(blockX, blockY, blockZ);
                    if(world.getBlockState(checkPos).getBlock().equals(block))
                    {
                        return true;
                    }
                }
                blockZ = pos.getZ() - range;
            }
            blockY = pos.getY() - range;
        }
        return false;
    }





}
