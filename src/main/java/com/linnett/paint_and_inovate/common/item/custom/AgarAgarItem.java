package com.linnett.paint_and_inovate.common.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;

public class AgarAgarItem extends Item {
    public AgarAgarItem() {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(0)
                        .saturationModifier(0)
                        .alwaysEdible()
                        .build())
                .stacksTo(16)
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    player.addEffect(new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration() + 850,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ));
                }
                stack.shrink(1);
            }
            else {
                spawnSubtleParticles(level, player);
            }
        }
        return stack;
    }

    private void spawnSubtleParticles(Level level, Player player) {
        for (int i = 0; i < 5; i++) {
            double radius = 0.8;
            double angle = level.random.nextDouble() * Math.PI * 2;
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;

            double yOffset = level.random.nextDouble() * player.getBbHeight();

            level.addParticle(ParticleTypes.END_ROD,
                    player.getX() + xOffset,
                    player.getY() + yOffset,
                    player.getZ() + zOffset,
                    0,
                    0.05,
                    0
            );
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }
}