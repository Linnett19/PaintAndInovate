package com.linnett.paint_and_inovate.common.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class PointerItem extends Item {

    public PointerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            Vec3 eyePosition = player.getEyePosition(1.0F);
            Vec3 lookVector = player.getLookAngle();
            Vec3 reachEnd = eyePosition.add(lookVector.scale(20.0D));

            AABB searchBox = player.getBoundingBox().expandTowards(lookVector.scale(10.0D)).inflate(1.0D);
            EntityHitResult entityHitResult = getEntityHitResult(level, player, eyePosition, reachEnd, searchBox);

            if (entityHitResult != null) {
                Entity targetEntity = entityHitResult.getEntity();
                if (targetEntity instanceof LivingEntity) {
                    ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 0, false, false));
                }
            } else {
                BlockHitResult blockHitResult = level.clip(new ClipContext(eyePosition, reachEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    spawnEndRodParticles((ServerLevel) level, blockPos);
                }
            }
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    private EntityHitResult getEntityHitResult(Level level, Player player, Vec3 startVec, Vec3 endVec, AABB boundingBox) {
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, player, startVec, endVec, boundingBox, (entity) -> !entity.isSpectator() && entity.isPickable());
        return entityHitResult;
    }

    private void spawnEndRodParticles(ServerLevel level, BlockPos pos) {
        double[][] offsets = {
                {0.0D, 0.0D, 0.0D}, {1.0D, 0.0D, 0.0D}, {0.0D, 0.0D, 1.0D}, {1.0D, 0.0D, 1.0D},
                {0.0D, 1.0D, 0.0D}, {1.0D, 1.0D, 0.0D}, {0.0D, 1.0D, 1.0D}, {1.0D, 1.0D, 1.0D}
        };

        for (double[] offset : offsets) {
            double x = pos.getX() + offset[0];
            double y = pos.getY() + offset[1];
            double z = pos.getZ() + offset[2];
            level.sendParticles(ParticleTypes.END_ROD, x, y, z, 5, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}
