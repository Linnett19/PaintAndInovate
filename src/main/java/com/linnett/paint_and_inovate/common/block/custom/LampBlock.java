package com.linnett.paint_and_inovate.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LampBlock extends Block {
    private static final VoxelShape SHAPE = Shapes.box(
            0.3333f, 0.01f, 0.3333f,  // minX, minY, minZ (отступ 45% сверху и снизу)
            0.6666f, 0.93f, 0.6666f   // maxX, maxY, maxZ (итого 10% высоты
    );

    public LampBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .noOcclusion()
                .lightLevel(state -> 15));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}