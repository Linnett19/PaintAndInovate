package com.linnett.paint_and_inovate.common.event;

import com.linnett.paint_and_inovate.Paint_and_inovate;
import com.linnett.paint_and_inovate.common.entity.HCEntities;
import com.linnett.paint_and_inovate.common.entity.client.RoboCreeperModel;
import com.linnett.paint_and_inovate.common.entity.custom.RoboCreeperEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Paint_and_inovate.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(RoboCreeperModel.LAYER_LOCATION, RoboCreeperModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(HCEntities.ROBO_CREEPER.get(), RoboCreeperEntity.createAttributes().build());

    }
}