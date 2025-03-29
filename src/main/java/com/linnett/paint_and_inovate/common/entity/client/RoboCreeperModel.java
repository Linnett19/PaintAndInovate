package com.linnett.paint_and_inovate.common.entity.client;

import com.linnett.paint_and_inovate.Paint_and_inovate;
import com.linnett.paint_and_inovate.common.entity.custom.RoboCreeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RoboCreeperModel<T extends RoboCreeperEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("paint_and_inovate", "robo_creeper"), "main");
    private final ModelPart root;
    private final ModelPart body_root;
    private final ModelPart body;
    private final ModelPart head;


    public RoboCreeperModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body_root = this.root.getChild("body_root");
        this.body = this.body_root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_root = root.addOrReplaceChild("body_root", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = body_root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -4.5F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.5F, -4.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -21.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition leg = body_root.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-5.0F, -12.0F, 5.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition leg2 = body_root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 16).mirror().addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(5.0F, -12.0F, 5.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition leg3 = body_root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 16).mirror().addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-5.0F, -12.0F, -5.0F));

        PartDefinition leg4 = body_root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 13.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(5.0F, -12.0F, -5.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(RoboCreeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(RoboCreeperAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, RoboCreeperAnimations.IDEL, ageInTicks, 2f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}