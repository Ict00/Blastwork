package com.ist.blastwork.recipe.FluidBarrelRecipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidBarrelRecipeInput(FluidStack fluid, ItemStack stack) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        if (index == 0) return this.stack;
        throw new IllegalArgumentException("No item for index " + index);
    }

    @Override
    public int size() {
        return 1;
    }
}
