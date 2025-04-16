package com.linnett.paint_and_inovate;

import com.linnett.paint_and_inovate.common.block.ModBlockRegistry;
import com.linnett.paint_and_inovate.common.entity.HCEntities;
import com.linnett.paint_and_inovate.common.entity.client.RoboCreeperRenderer;
import com.linnett.paint_and_inovate.common.item.ModCreativeTabs;
import com.linnett.paint_and_inovate.common.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Paint_and_inovate.MODID)
public class Paint_and_inovate {
    public static final String MODID = "paint_and_inovate";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Paint_and_inovate(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        HCEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);


        ModItems.register(modEventBus);
        ModBlockRegistry.register(modEventBus);
        modEventBus.addListener(this::commonSetup);


        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event){
    }

    public static void init(){
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = "paint_and_inovate", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(HCEntities.ROBO_CREEPER.get(), RoboCreeperRenderer::new);
        }
    }

}
