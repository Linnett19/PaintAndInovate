package com.linnett.paint_and_inovate.common.item;

import com.linnett.paint_and_inovate.Paint_and_inovate;
import com.linnett.paint_and_inovate.common.item.custom.ElixirOfDoubling;
import com.linnett.paint_and_inovate.common.item.custom.PaintBrushItem;
import com.linnett.paint_and_inovate.common.item.custom.PointerItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Paint_and_inovate.MODID);


    public static final DeferredItem<Item> POINTER = ITEMS.register("pointer",
            () -> new PointerItem());

    public static final DeferredItem<Item> ELIXIR_DOUBLING = ITEMS.register("elixir_doubling",
            () -> new ElixirOfDoubling());

    public static final DeferredItem<Item> PAINT_ROLLER = ITEMS.register("paint_roller",
            () -> new PaintBrushItem());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
