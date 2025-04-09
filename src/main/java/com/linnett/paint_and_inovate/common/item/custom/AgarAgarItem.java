package com.linnett.paint_and_inovate.common.item.custom;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.particles.ParticleTypes;

public class AgarAgarItem extends PotionItem {

    public AgarAgarItem() {
        super(new Item.Properties().food(new FoodProperties.Builder().build()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (effect.getDuration() > 0) {
                    int newDuration = effect.getDuration() + 2000;
                    player.addEffect(new MobEffectInstance(effect.getEffect(), newDuration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                }
            }

            for (int i = 0; i < 10; i++) {
                world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                        player.getX() + Math.random() - 0.5,
                        player.getY() + Math.random(),
                        player.getZ() + Math.random() - 0.5,
                        0, 0, 0);
            }

            itemStack.shrink(1);
        }

        player.getCooldowns().addCooldown(this, 32);

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }
}

