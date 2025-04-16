package com.linnett.paint_and_inovate.common.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

public class PaintBrushItem extends Item {
    public PaintBrushItem() {
        super(new Properties().durability(256).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack offHandItem = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        if (hand == InteractionHand.MAIN_HAND || !offHandItem.getItem().equals(this)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (!(mainHandItem.getItem() instanceof DyeItem)) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        DyeItem dyeItem = (DyeItem) mainHandItem.getItem();
        DyeColor dyeColor = dyeItem.getDyeColor();
        HitResult result = player.pick(5.0D, 1.0F, false);
        if (result.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        BlockHitResult blockHitResult = (BlockHitResult) result;
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (isPaintableBlock(blockState.getBlock())) {
            Block newBlock = getColoredBlock(blockState.getBlock(), dyeColor);
            if (newBlock != null) {
                level.setBlockAndUpdate(blockPos, newBlock.defaultBlockState());
                spawnPaintParticles(level, blockPos, dyeColor);
                if (!player.getAbilities().instabuild) {mainHandItem.shrink(1);}
            }
        } else {
            player.displayClientMessage(
                    Component.translatable("message.paint_and_inovate.cannot_paint")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    private boolean isPaintableBlock(Block block) {
        return block == Blocks.WHITE_WOOL || block == Blocks.ORANGE_WOOL || block == Blocks.MAGENTA_WOOL
                || block == Blocks.LIGHT_BLUE_WOOL || block == Blocks.YELLOW_WOOL || block == Blocks.LIME_WOOL
                || block == Blocks.PINK_WOOL || block == Blocks.GRAY_WOOL || block == Blocks.LIGHT_GRAY_WOOL
                || block == Blocks.CYAN_WOOL || block == Blocks.PURPLE_WOOL || block == Blocks.BLUE_WOOL
                || block == Blocks.BROWN_WOOL || block == Blocks.GREEN_WOOL || block == Blocks.RED_WOOL
                || block == Blocks.BLACK_WOOL
                || block == Blocks.WHITE_CONCRETE || block == Blocks.ORANGE_CONCRETE || block == Blocks.MAGENTA_CONCRETE
                || block == Blocks.LIGHT_BLUE_CONCRETE || block == Blocks.YELLOW_CONCRETE || block == Blocks.LIME_CONCRETE
                || block == Blocks.PINK_CONCRETE || block == Blocks.GRAY_CONCRETE || block == Blocks.LIGHT_GRAY_CONCRETE
                || block == Blocks.CYAN_CONCRETE || block == Blocks.PURPLE_CONCRETE || block == Blocks.BLUE_CONCRETE
                || block == Blocks.BROWN_CONCRETE || block == Blocks.GREEN_CONCRETE || block == Blocks.RED_CONCRETE
                || block == Blocks.BLACK_CONCRETE
                || block == Blocks.WHITE_TERRACOTTA || block == Blocks.ORANGE_TERRACOTTA || block == Blocks.MAGENTA_TERRACOTTA
                || block == Blocks.LIGHT_BLUE_TERRACOTTA || block == Blocks.YELLOW_TERRACOTTA || block == Blocks.LIME_TERRACOTTA
                || block == Blocks.PINK_TERRACOTTA || block == Blocks.GRAY_TERRACOTTA || block == Blocks.LIGHT_GRAY_TERRACOTTA
                || block == Blocks.CYAN_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA || block == Blocks.BLUE_TERRACOTTA
                || block == Blocks.BROWN_TERRACOTTA || block == Blocks.GREEN_TERRACOTTA || block == Blocks.RED_TERRACOTTA
                || block == Blocks.BLACK_TERRACOTTA

                || block instanceof ConcretePowderBlock
                || block instanceof GlazedTerracottaBlock
                || block instanceof CarpetBlock;
    }
    private Block getColoredBlock(Block block, DyeColor dyeColor) {
        if (block == Blocks.WHITE_WOOL || block == Blocks.ORANGE_WOOL || block == Blocks.MAGENTA_WOOL
                || block == Blocks.LIGHT_BLUE_WOOL || block == Blocks.YELLOW_WOOL || block == Blocks.LIME_WOOL
                || block == Blocks.PINK_WOOL || block == Blocks.GRAY_WOOL || block == Blocks.LIGHT_GRAY_WOOL
                || block == Blocks.CYAN_WOOL || block == Blocks.PURPLE_WOOL || block == Blocks.BLUE_WOOL
                || block == Blocks.BROWN_WOOL || block == Blocks.GREEN_WOOL || block == Blocks.RED_WOOL
                || block == Blocks.BLACK_WOOL) {
            return getWoolBlock(dyeColor);
        }
        if (block == Blocks.WHITE_CONCRETE || block == Blocks.ORANGE_CONCRETE || block == Blocks.MAGENTA_CONCRETE
                || block == Blocks.LIGHT_BLUE_CONCRETE || block == Blocks.YELLOW_CONCRETE || block == Blocks.LIME_CONCRETE
                || block == Blocks.PINK_CONCRETE || block == Blocks.GRAY_CONCRETE || block == Blocks.LIGHT_GRAY_CONCRETE
                || block == Blocks.CYAN_CONCRETE || block == Blocks.PURPLE_CONCRETE || block == Blocks.BLUE_CONCRETE
                || block == Blocks.BROWN_CONCRETE || block == Blocks.GREEN_CONCRETE || block == Blocks.RED_CONCRETE
                || block == Blocks.BLACK_CONCRETE) {
            return getConcreteBlock(dyeColor);
        }
        if (block == Blocks.WHITE_CARPET || block == Blocks.ORANGE_CARPET || block == Blocks.MAGENTA_CARPET
                || block == Blocks.LIGHT_BLUE_CARPET || block == Blocks.YELLOW_CARPET || block == Blocks.LIME_CARPET
                || block == Blocks.PINK_CARPET || block == Blocks.GRAY_CARPET || block == Blocks.LIGHT_GRAY_CARPET
                || block == Blocks.CYAN_CARPET || block == Blocks.PURPLE_CARPET || block == Blocks.BLUE_CARPET
                || block == Blocks.BROWN_CARPET || block == Blocks.GREEN_CARPET || block == Blocks.RED_CARPET
                || block == Blocks.BLACK_CARPET) {
            return getCarpetBlock(dyeColor);
        }
        if (block == Blocks.WHITE_CONCRETE_POWDER || block == Blocks.ORANGE_CONCRETE_POWDER || block == Blocks.MAGENTA_CONCRETE_POWDER
                || block == Blocks.LIGHT_BLUE_CONCRETE_POWDER || block == Blocks.YELLOW_CONCRETE_POWDER || block == Blocks.LIME_CONCRETE_POWDER
                || block == Blocks.PINK_CONCRETE_POWDER || block == Blocks.GRAY_CONCRETE_POWDER || block == Blocks.LIGHT_GRAY_CONCRETE_POWDER
                || block == Blocks.CYAN_CONCRETE_POWDER || block == Blocks.PURPLE_CONCRETE_POWDER || block == Blocks.BLUE_CONCRETE_POWDER
                || block == Blocks.BROWN_CONCRETE_POWDER || block == Blocks.GREEN_CONCRETE_POWDER || block == Blocks.RED_CONCRETE_POWDER
                || block == Blocks.BLACK_CONCRETE_POWDER) {
            return getConcretePowdedBlock(dyeColor);
        }
        if (block == Blocks.WHITE_GLAZED_TERRACOTTA || block == Blocks.ORANGE_GLAZED_TERRACOTTA || block == Blocks.MAGENTA_GLAZED_TERRACOTTA
                || block == Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA || block == Blocks.YELLOW_GLAZED_TERRACOTTA || block == Blocks.LIME_GLAZED_TERRACOTTA
                || block == Blocks.PINK_GLAZED_TERRACOTTA || block == Blocks.GRAY_GLAZED_TERRACOTTA || block == Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA
                || block == Blocks.CYAN_GLAZED_TERRACOTTA || block == Blocks.PURPLE_GLAZED_TERRACOTTA || block == Blocks.BLUE_GLAZED_TERRACOTTA
                || block == Blocks.BROWN_GLAZED_TERRACOTTA || block == Blocks.GREEN_GLAZED_TERRACOTTA || block == Blocks.RED_GLAZED_TERRACOTTA
                || block == Blocks.BLACK_GLAZED_TERRACOTTA) {
            return getGlazedTerracottaBlock(dyeColor);
        }
        if (block == Blocks.WHITE_TERRACOTTA || block == Blocks.ORANGE_TERRACOTTA || block == Blocks.MAGENTA_TERRACOTTA
                || block == Blocks.LIGHT_BLUE_TERRACOTTA || block == Blocks.YELLOW_TERRACOTTA || block == Blocks.LIME_TERRACOTTA
                || block == Blocks.PINK_TERRACOTTA || block == Blocks.GRAY_TERRACOTTA || block == Blocks.LIGHT_GRAY_TERRACOTTA
                || block == Blocks.CYAN_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA || block == Blocks.BLUE_TERRACOTTA
                || block == Blocks.BROWN_TERRACOTTA || block == Blocks.GREEN_TERRACOTTA || block == Blocks.RED_TERRACOTTA
                || block == Blocks.BLACK_TERRACOTTA) {
            return getTerracottaBlock(dyeColor);
        }
        return null;
    }
    private Block getWoolBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_WOOL;
            case ORANGE: return Blocks.ORANGE_WOOL;
            case MAGENTA: return Blocks.MAGENTA_WOOL;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_WOOL;
            case YELLOW: return Blocks.YELLOW_WOOL;
            case LIME: return Blocks.LIME_WOOL;
            case PINK: return Blocks.PINK_WOOL;
            case GRAY: return Blocks.GRAY_WOOL;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_WOOL;
            case CYAN: return Blocks.CYAN_WOOL;
            case PURPLE: return Blocks.PURPLE_WOOL;
            case BLUE: return Blocks.BLUE_WOOL;
            case BROWN: return Blocks.BROWN_WOOL;
            case GREEN: return Blocks.GREEN_WOOL;
            case RED: return Blocks.RED_WOOL;
            case BLACK: return Blocks.BLACK_WOOL;
            default: return null;
        }
    }
    private Block getGlazedTerracottaBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_GLAZED_TERRACOTTA;
            case ORANGE: return Blocks.ORANGE_GLAZED_TERRACOTTA;
            case MAGENTA: return Blocks.MAGENTA_GLAZED_TERRACOTTA;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA;
            case YELLOW: return Blocks.YELLOW_GLAZED_TERRACOTTA;
            case LIME: return Blocks.LIME_GLAZED_TERRACOTTA;
            case PINK: return Blocks.PINK_GLAZED_TERRACOTTA;
            case GRAY: return Blocks.GRAY_GLAZED_TERRACOTTA;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA;
            case CYAN: return Blocks.CYAN_GLAZED_TERRACOTTA;
            case PURPLE: return Blocks.PURPLE_GLAZED_TERRACOTTA;
            case BLUE: return Blocks.BLUE_GLAZED_TERRACOTTA;
            case BROWN: return Blocks.BROWN_GLAZED_TERRACOTTA;
            case GREEN: return Blocks.GREEN_GLAZED_TERRACOTTA;
            case RED: return Blocks.RED_GLAZED_TERRACOTTA;
            case BLACK: return Blocks.BLACK_GLAZED_TERRACOTTA;
            default: return null;
        }
    }
    private Block getConcretePowdedBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_CONCRETE_POWDER;
            case ORANGE: return Blocks.ORANGE_CONCRETE_POWDER;
            case MAGENTA: return Blocks.MAGENTA_CONCRETE_POWDER;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CONCRETE_POWDER;
            case YELLOW: return Blocks.YELLOW_CONCRETE_POWDER;
            case LIME: return Blocks.LIME_CONCRETE_POWDER;
            case PINK: return Blocks.PINK_CONCRETE_POWDER;
            case GRAY: return Blocks.GRAY_CONCRETE_POWDER;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CONCRETE_POWDER;
            case CYAN: return Blocks.CYAN_CONCRETE_POWDER;
            case PURPLE: return Blocks.PURPLE_CONCRETE_POWDER;
            case BLUE: return Blocks.BLUE_CONCRETE_POWDER;
            case BROWN: return Blocks.BROWN_CONCRETE_POWDER;
            case GREEN: return Blocks.GREEN_CONCRETE_POWDER;
            case RED: return Blocks.RED_CONCRETE_POWDER;
            case BLACK: return Blocks.BLACK_CONCRETE_POWDER;
            default: return null;
        }
    }
    private Block getCarpetBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_CARPET;
            case ORANGE: return Blocks.ORANGE_CARPET;
            case MAGENTA: return Blocks.MAGENTA_CARPET;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CARPET;
            case YELLOW: return Blocks.YELLOW_CARPET;
            case LIME: return Blocks.LIME_CARPET;
            case PINK: return Blocks.PINK_CARPET;
            case GRAY: return Blocks.GRAY_CARPET;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CARPET;
            case CYAN: return Blocks.CYAN_CARPET;
            case PURPLE: return Blocks.PURPLE_CARPET;
            case BLUE: return Blocks.BLUE_CARPET;
            case BROWN: return Blocks.BROWN_CARPET;
            case GREEN: return Blocks.GREEN_CARPET;
            case RED: return Blocks.RED_CARPET;
            case BLACK: return Blocks.BLACK_CARPET;
            default: return null;
        }
    }
    private Block getConcreteBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_CONCRETE;
            case ORANGE: return Blocks.ORANGE_CONCRETE;
            case MAGENTA: return Blocks.MAGENTA_CONCRETE;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_CONCRETE;
            case YELLOW: return Blocks.YELLOW_CONCRETE;
            case LIME: return Blocks.LIME_CONCRETE;
            case PINK: return Blocks.PINK_CONCRETE;
            case GRAY: return Blocks.GRAY_CONCRETE;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_CONCRETE;
            case CYAN: return Blocks.CYAN_CONCRETE;
            case PURPLE: return Blocks.PURPLE_CONCRETE;
            case BLUE: return Blocks.BLUE_CONCRETE;
            case BROWN: return Blocks.BROWN_CONCRETE;
            case GREEN: return Blocks.GREEN_CONCRETE;
            case RED: return Blocks.RED_CONCRETE;
            case BLACK: return Blocks.BLACK_CONCRETE;
            default: return null;
        }
    }
    private Block getTerracottaBlock(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return Blocks.WHITE_TERRACOTTA;
            case ORANGE: return Blocks.ORANGE_TERRACOTTA;
            case MAGENTA: return Blocks.MAGENTA_TERRACOTTA;
            case LIGHT_BLUE: return Blocks.LIGHT_BLUE_TERRACOTTA;
            case YELLOW: return Blocks.YELLOW_TERRACOTTA;
            case LIME: return Blocks.LIME_TERRACOTTA;
            case PINK: return Blocks.PINK_TERRACOTTA;
            case GRAY: return Blocks.GRAY_TERRACOTTA;
            case LIGHT_GRAY: return Blocks.LIGHT_GRAY_TERRACOTTA;
            case CYAN: return Blocks.CYAN_TERRACOTTA;
            case PURPLE: return Blocks.PURPLE_TERRACOTTA;
            case BLUE: return Blocks.BLUE_TERRACOTTA;
            case BROWN: return Blocks.BROWN_TERRACOTTA;
            case GREEN: return Blocks.GREEN_TERRACOTTA;
            case RED: return Blocks.RED_TERRACOTTA;
            case BLACK: return Blocks.BLACK_TERRACOTTA;
            default: return null;
        }
    }
    private void spawnPaintParticles(Level level, BlockPos pos, DyeColor dyeColor) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        Vector3f color = getColorForDye(dyeColor);
        double radius = 0.8;
        int numParticles = 5;
        for (int i = 0; i < numParticles; i++) {
            double offsetX = (Math.random() * 2 - 1);
            double offsetZ = (Math.random() * 2 - 1);
            double offsetY = (Math.random() * 2 - 1) * 0.5;
            double velX = (Math.random() - 0.5) * 0.1;
            double velY = (Math.random() - 0.5) * 0.1;
            double velZ = (Math.random() - 0.5) * 0.1;
            level.addParticle(
                    new DustParticleOptions(color, 1.0f),
                    x + offsetX, y + offsetY, z + offsetZ,
                    velX, velY, velZ
            );
        }
    }

    private Vector3f getColorForDye(DyeColor dyeColor) {
        switch (dyeColor) {
            case WHITE: return new Vector3f(1.0f, 1.0f, 1.0f);
            case ORANGE: return new Vector3f(1.0f, 0.5f, 0.0f);
            case MAGENTA: return new Vector3f(1.0f, 0.0f, 1.0f);
            case LIGHT_BLUE: return new Vector3f(0.5f, 0.5f, 1.0f);
            case YELLOW: return new Vector3f(1.0f, 1.0f, 0.0f);
            case LIME: return new Vector3f(0.5f, 1.0f, 0.0f);
            case PINK: return new Vector3f(1.0f, 0.75f, 0.8f);
            case GRAY: return new Vector3f(0.5f, 0.5f, 0.5f);
            case LIGHT_GRAY: return new Vector3f(0.75f, 0.75f, 0.75f);
            case CYAN: return new Vector3f(0.0f, 1.0f, 1.0f);
            case PURPLE: return new Vector3f(0.5f, 0.0f, 0.5f);
            case BLUE: return new Vector3f(0.0f, 0.0f, 1.0f);
            case BROWN: return new Vector3f(0.6f, 0.3f, 0.0f);
            case GREEN: return new Vector3f(0.0f, 1.0f, 0.0f);
            case RED: return new Vector3f(1.0f, 0.0f, 0.0f);
            case BLACK: return new Vector3f(0.0f, 0.0f, 0.0f);
            default: return new Vector3f(1.0f, 1.0f, 1.0f);
        }
    }
}


