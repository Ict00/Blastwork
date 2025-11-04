package com.ist.blastwork.recipe;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.recipe.FluidBarrelRecipe.FluidBarrelRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Blastwork.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Blastwork.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidBarrelRecipe>> FLUID_BARREL_RECIPE_SERIALIZER =
            SERIALIZERS.register("fluid_barrel_recipe", FluidBarrelRecipe.FB_Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FluidBarrelRecipe>> FLUID_BARREL_RECIPE_TYPE =
            TYPES.register("fluid_barrel_recipe", () -> new RecipeType<FluidBarrelRecipe>() {
                @Override
                public String toString() {
                    return "fluid_barrel_recipe";
                }
            });

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }
}
