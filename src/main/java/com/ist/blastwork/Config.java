package com.ist.blastwork;

import java.util.List;

import com.ist.blastwork.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ModConfigSpec;

// TODO: I WILL NEED TO CHANGE THIS ON RELEASE
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue USE_NEW_EXPLOSION_DAMAGE_SYSTEM =
            BUILDER.define("newExplosionDamageCalculation", true);


    public static final ModConfigSpec.BooleanValue FLUID_BARREL_FLUID_MOVEMENT =
            BUILDER.define("fluidBarrelFluidMovement", true);

    public static final ModConfigSpec.IntValue FLUID_BARREL_CAPACITY = BUILDER
            .defineInRange("fluidBarrelCapacity", 5000, 5000, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
