package com.linnett.paint_and_inovate.common.block;

import com.linnett.paint_and_inovate.Paint_and_inovate;
import com.linnett.paint_and_inovate.common.block.custom.LampBlock;
import com.linnett.paint_and_inovate.common.block.custom.LatticeBlock;
import com.linnett.paint_and_inovate.common.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Paint_and_inovate.MODID);

    public static final DeferredBlock<Block> COPPER_LATTICE = registerBlock("copper_lattice",
            () -> new LatticeBlock());
    public static final DeferredBlock<Block> EXPOSED_COPPER_LATTICE = registerBlock("exposed_copper_lattice",
            () -> new LatticeBlock());
    public static final DeferredBlock<Block> WEATHERED_COPPER_LATTICE = registerBlock("weathered_copper_lattice",
            () -> new LatticeBlock());
    public static final DeferredBlock<Block> OXIDIZED_COPPER_LATTICE = registerBlock("oxidized_copper_lattice",
            () -> new LatticeBlock());


    public static final DeferredBlock<Block> COPPER_COLUMN = registerBlock("copper_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()
                    .sound(SoundType.TUFF)));

    public static final DeferredBlock<Block> EXPOSED_COPPER_COLUMN = registerBlock("exposed_copper_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()
                    .sound(SoundType.TUFF)));

    public static final DeferredBlock<Block> WEATHERED_COPPER_COLUMN = registerBlock("weathered_copper_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()
                    .sound(SoundType.TUFF)));

    public static final DeferredBlock<Block> OXIDIZED_COPPER_COLUMN = registerBlock("oxidized_copper_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()
                    .sound(SoundType.TUFF)));




    public static final DeferredBlock<Block> LAMP = registerBlock("lamp",
            () -> new LampBlock());


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
