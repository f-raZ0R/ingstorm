package net.fraZ0R.ingstorm.common;

import net.fraZ0R.ingstorm.Ingstorm;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BlockProximity {

    /**
     * {@link TagKey}s for different ranges
     *
     * @author Oliver-makes-code
     * */
    public static TagKey<Block> RANGE_0 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_0"));
    public static TagKey<Block> RANGE_2 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_2"));
    public static TagKey<Block> RANGE_4 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_4"));
    public static TagKey<Block> RANGE_8 = TagKey.of(Registry.BLOCK_KEY, new Identifier(Ingstorm.modid, "range_8"));

    public static boolean isSafe(BlockPos pos, World world) {
        // Check range 0
        if (world.getBlockState(pos).isIn(RANGE_0)) {
            return true;
        }

        // Get an iterable of all BlockPoses in an 8x8 cube
        Iterable<BlockPos> iterate = BlockPos.iterate(
                pos.down(8).south(8).west(8),
                pos.up(8).north(8).east(8)
        );

        // Iterate through all those BlockPoses
        for (BlockPos current : iterate) {
            // check if within 2 blocks
            if (current.isWithinDistance(pos, 2) && world.getBlockState(current).isIn(RANGE_2)) {
                return true;
            }
            // Check if within 4 blocks
            if (current.isWithinDistance(pos, 4) && world.getBlockState(current).isIn(RANGE_4)) {
                return true;
            }
            // Check if within 8 blocks
            if (current.isWithinDistance(pos, 8) && world.getBlockState(current).isIn(RANGE_8)) {
                return true;
            }
        }
        return false;
    }
}
