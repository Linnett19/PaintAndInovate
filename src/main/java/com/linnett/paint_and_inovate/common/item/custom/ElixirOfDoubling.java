package com.linnett.paint_and_inovate.common.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

public class ElixirOfDoubling extends Item {
    public ElixirOfDoubling() {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(0)
                        .saturationModifier(0)
                        .alwaysEdible()
                        .build())
                .stacksTo(16)
                .rarity(Rarity.UNCOMMON)
        );
    }




    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        {
            tooltipComponents.add(Component.translatable("item.paint_and_inovate.effect").withStyle(ChatFormatting.BLUE));
            tooltipComponents.add(Component.translatable("item.paint_and_inovate.elixir_doubling.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }






    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
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

                level.playSound(null, player.blockPosition(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0f, 1.0f);
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5f,
                        level.random.nextFloat() * 0.1f + 0.9f);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } else {
                spawnSubtleParticles(level, player);
            }
        }
        return stack;
    }

    private void spawnSubtleParticles(Level level, Player player) {
        Vector3f color = new Vector3f(0.38f, 0.84f, 0.87f);
        for (int i = 0; i < 15; i++) {
            double radius = 0.8;
            double angle = level.random.nextDouble() * Math.PI * 2;
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            double yOffset = level.random.nextDouble() * player.getBbHeight();

            double velX = (level.random.nextDouble() - 0.5) * 0.02;
            double velY = 0.05 + level.random.nextDouble() * 0.02;
            double velZ = (level.random.nextDouble() - 0.5) * 0.02;

            level.addParticle(
                    new DustParticleOptions(color, 1.5f),
                    player.getX() + xOffset,
                    player.getY() + yOffset,
                    player.getZ() + zOffset,
                    velX, velY, velZ
            );
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}