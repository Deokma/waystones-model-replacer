package dev.deokma.wmr.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.Map;

/**
 * Replaces Waystones' hardcoded {@code WaystoneBlock.getShape} (the chunky monument hitbox) with a
 * slim hitbox that fits the custom signpost model: a thin central post + flat base for the lower
 * half, and the post + a thin sign-board volume for the upper half.
 *
 * <p>The upper board is offset toward one side (like the model), so it is rotated per horizontal
 * FACING. The rotation matches the model's rendered orientation, which this addon's resource pack
 * rotates by +180 degrees over the vanilla Waystones blockstate (see
 * {@code assets/waystones/blockstates/waystone.json}). The authored base shape corresponds to
 * FACING=SOUTH (model rendered at 0 degrees); the other facings rotate it 90/180/270 degrees,
 * matching {@code (waystonesBlockstateY + 180) % 360}.
 *
 * <p>Not a client-only mixin (collision/outline shapes are needed server-side too); listed under the
 * top-level {@code "mixins"} list. No Waystones class is imported - HALF/FACING are read via the
 * vanilla properties Waystones reuses - so this still compiles in the loader-agnostic common module.
 */
@Mixin(targets = "net.blay09.mods.waystones.block.WaystoneBlock")
public class WaystoneBlockShapeMixin {

    @Unique
    private static final VoxelShape wmr$LOWER = Shapes.or(
            Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0),   // central post
            Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0)   // base plate
    ).optimize();

    // Authored for FACING=SOUTH (sign board toward +Z, matching the model at 0 degrees rotation).
    @Unique
    private static final VoxelShape wmr$UPPER_BASE = Shapes.or(
            Block.box(7.0, 0.0, 7.0, 9.0, 15.0, 9.0),    // post continues
            Block.box(2.0, 4.0, 8.0, 14.0, 16.0, 12.0)   // sign board + arms (one-sided, slim)
    ).optimize();

    @Unique
    private static final Map<Direction, VoxelShape> wmr$UPPER = new EnumMap<>(Direction.class);

    static {
        wmr$UPPER.put(Direction.SOUTH, wmr$rotateY(wmr$UPPER_BASE, 0));
        wmr$UPPER.put(Direction.WEST, wmr$rotateY(wmr$UPPER_BASE, 1));
        wmr$UPPER.put(Direction.NORTH, wmr$rotateY(wmr$UPPER_BASE, 2));
        wmr$UPPER.put(Direction.EAST, wmr$rotateY(wmr$UPPER_BASE, 3));
    }

    /** Rotates a shape clockwise (viewed from above) by {@code quarterTurns} * 90 degrees, matching blockstate "y". */
    @Unique
    private static VoxelShape wmr$rotateY(VoxelShape shape, int quarterTurns) {
        VoxelShape result = shape;
        for (int t = 0; t < quarterTurns; t++) {
            final VoxelShape src = result;
            final VoxelShape[] acc = {Shapes.empty()};
            src.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
                    acc[0] = Shapes.or(acc[0], Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2)));
            result = acc[0].optimize();
        }
        return result;
    }

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void wmr$slimSignpostShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            cir.setReturnValue(wmr$LOWER);
        } else {
            cir.setReturnValue(wmr$UPPER.getOrDefault(
                    state.getValue(BlockStateProperties.HORIZONTAL_FACING), wmr$UPPER_BASE));
        }
    }
}
