package com.linnett.paint_and_inovate.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class MirrorBlock extends Block {
    public MirrorBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .noOcclusion()
                .isSuffocating((state, world, pos) -> false)
                .isViewBlocking((state, world, pos) -> false)
                .isRedstoneConductor((state, world, pos) -> false)
                .pushReaction(PushReaction.NORMAL)
                .strength(0.3F)
                .sound(SoundType.GLASS));
    }


    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }
}