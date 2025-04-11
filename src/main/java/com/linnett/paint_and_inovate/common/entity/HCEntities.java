package com.linnett.paint_and_inovate.common.entity;

import com.linnett.paint_and_inovate.Paint_and_inovate;
import com.linnett.paint_and_inovate.common.entity.custom.RoboCreeperEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Paint_and_inovate.MODID);

    public static final Supplier<EntityType<RoboCreeperEntity>> ROBO_CREEPER =
            ENTITY_TYPES.register("robo_creeper",
                    () -> EntityType.Builder.<RoboCreeperEntity>of(RoboCreeperEntity::new, MobCategory.MONSTER)
                            .sized(1.15f, 1.4f)
                            .build(ResourceLocation.fromNamespaceAndPath(Paint_and_inovate.MODID, "robo_creeper").toString())
            );






    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}