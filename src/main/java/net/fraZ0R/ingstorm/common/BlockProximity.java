package net.fraZ0R.ingstorm.common;

import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockProximity {

    /**
     * {@link TagKey}s for different ranges
     *
     * @author Oliver-makes-code
     * */

    public static boolean isInRange(BlockPos pos, World world, TagKey key, int range) {
        // Check range 0
        range+=1;

        // Get an iterable of all BlockPoses in an arbitrarily sized cube
        Iterable<BlockPos> iterate = BlockPos.iterate(
                pos.down(range).south(range).west(range),
                pos.up(range).north(range).east(range)
        );

        // Iterate through all those BlockPoses
        for (BlockPos current : iterate) {
            // check if within 2 blocks
            if (current.isWithinDistance(pos, range) && world.getBlockState(current).isIn(key)) {
                return true;
            }
        }
        return false;
    }
}
