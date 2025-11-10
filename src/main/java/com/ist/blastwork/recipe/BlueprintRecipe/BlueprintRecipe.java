package com.ist.blastwork.recipe.BlueprintRecipe;

import com.ist.blastwork.recipe.FluidBarrelRecipe.FluidBarrelRecipe;
import com.ist.blastwork.recipe.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record BlueprintRecipe(List<Ingredient> ingredients, ItemStack result) implements Recipe<BlueprintRecipeInput> {
    @Override
    public boolean matches(BlueprintRecipeInput input, Level level) {
        if (ingredients.size() != input.size()) return false;

        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).test(input.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(BlueprintRecipeInput input, HolderLookup.Provider registries) {
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BLUEPRINT_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BLUEPRINT_RECIPE_TYPE.get();
    }


    public static class BlueprintR_Serializer implements RecipeSerializer<BlueprintRecipe> {
        public static final MapCodec<BlueprintRecipe> CODEC =
                RecordCodecBuilder.mapCodec(inst ->
                        inst.group(
                                Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter(BlueprintRecipe::ingredients),
                                ItemStack.CODEC.fieldOf("result").forGetter(BlueprintRecipe::result)
                        ).apply(inst, BlueprintRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BlueprintRecipe> STREAM_CODEC =
                StreamCodec.of(
                        BlueprintRecipe.BlueprintR_Serializer::toNetwork, BlueprintRecipe.BlueprintR_Serializer::fromNetwork
                );

        private static BlueprintRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            return new BlueprintRecipe(nonnulllist, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BlueprintRecipe recipe) {
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem(null));
        }
        @Override
        public MapCodec<BlueprintRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlueprintRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
