package com.linnett.paint_and_inovate.common.entity.client;

import com.linnett.paint_and_inovate.common.entity.custom.RoboCreeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RoboCreeperRenderer extends MobRenderer<RoboCreeperEntity, RoboCreeperModel<RoboCreeperEntity>> {

    public RoboCreeperRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RoboCreeperModel<>(pContext.bakeLayer(RoboCreeperModel.LAYER_LOCATION)), 0.85f);
    }


    @Override
    public void render(RoboCreeperEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1f, 1f, 1f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RoboCreeperEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("paint_and_inovate", "textures/entity/robo_creeper/robo_creeper.png");
    }


}
