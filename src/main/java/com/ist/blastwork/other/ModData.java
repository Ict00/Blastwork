package com.ist.blastwork.other;

import com.ist.blastwork.Blastwork;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class ModData {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Blastwork.MODID);

    public static final Supplier<DataComponentType<ResourceLocation>> BLUEPRINT_RECIPE_CONTAINER = DATA_COMPONENTS.register("recipe_container",
            () -> DataComponentType.<ResourceLocation>builder().persistent(ResourceLocation.CODEC).build());
    public static final Supplier<DataComponentType<Integer>> TIME_LEFT_TO_EXPLODE_DATA = DATA_COMPONENTS.register("time_left_to_explode",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<Integer>> SET_TIME_ON_EXPLODE_DATA = DATA_COMPONENTS.register("set_time_on_explode",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static final Supplier<DataComponentType<List<Vector3f>>> DETONATOR_TARGET_DATA = DATA_COMPONENTS.register("detonator_target",
            () -> DataComponentType.<List<Vector3f>>builder().persistent(Codec.list(ExtraCodecs.VECTOR3F)).build());

    public static final Supplier<DataComponentType<FluidStackComponent>> FLUID_DATA =
            DATA_COMPONENTS.register("fluid_stack",
                    () -> DataComponentType.<FluidStackComponent>builder()
                            .persistent(FluidStackComponent.CODEC)
                            .build());

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
