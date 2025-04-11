package com.linnett.paint_and_inovate.common.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;



@EventBusSubscriber(modid = "paint_and_inovate", bus = EventBusSubscriber.Bus.MOD)
public class DamageEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();


        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.getHolder(ModEffects.VULNERABILITY.getKey()).orElse(null);

        if (effect != null && entity.hasEffect(effect)) {
            float damage = event.getAmount();
            event.setAmount(damage * 1.1f);
        }
    }
}