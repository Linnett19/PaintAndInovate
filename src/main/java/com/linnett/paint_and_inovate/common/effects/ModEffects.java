package com.linnett.paint_and_inovate.common.effects;



import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, "paint_and_inovate");

    public static final Supplier<MobEffect> VULNERABILITY =
            EFFECTS.register("vulnerability", VulnerabilityEffect::new);
}