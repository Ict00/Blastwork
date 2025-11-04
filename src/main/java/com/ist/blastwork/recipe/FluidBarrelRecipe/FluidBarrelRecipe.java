package com.ist.blastwork.recipe.FluidBarrelRecipe;

import com.ist.blastwork.recipe.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidBarrelRecipe(FluidStack inputFluid, Ingredient inputItem, ItemStack result) implements Recipe<FluidBarrelRecipeInput> {

    @Override
    public boolean matches(FluidBarrelRecipeInput input, Level level) {
        if (level.isClientSide) return false;

        return this.inputFluid.is(input.fluid().getFluid()) && this.inputItem.test(input.getItem(0)) &&
                inputFluid.getAmount() <= input.fluid().getAmount();
    }

    @Override
    public ItemStack assemble(FluidBarrelRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> res = NonNullList.create();
        res.add(inputItem);
        return res;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLUID_BARREL_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLUID_BARREL_RECIPE_TYPE.get();
    }

    public static class FB_Serializer implements RecipeSerializer<FluidBarrelRecipe> {
        public static final MapCodec<FluidBarrelRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst ->
                        inst.group(
                            FluidStack.CODEC.fieldOf("fluid").forGetter(FluidBarrelRecipe::inputFluid),
                            Ingredient.CODEC_NONEMPTY.fieldOf("ingredientItem").forGetter(FluidBarrelRecipe::inputItem),
                            ItemStack.CODEC.fieldOf("result").forGetter(FluidBarrelRecipe::result)
                        ).apply(inst, FluidBarrelRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FluidBarrelRecipe> STREAM_CODEC =
                StreamCodec.composite(
                    FluidStack.STREAM_CODEC, FluidBarrelRecipe::inputFluid,
                    Ingredient.CONTENTS_STREAM_CODEC, FluidBarrelRecipe::inputItem,
                    ItemStack.STREAM_CODEC, FluidBarrelRecipe::result,
                    FluidBarrelRecipe::new
                );

        @Override
        public MapCodec<FluidBarrelRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FluidBarrelRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
