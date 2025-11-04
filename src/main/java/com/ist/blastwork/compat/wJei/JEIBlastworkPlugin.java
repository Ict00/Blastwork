package com.ist.blastwork.compat.wJei;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.recipe.FluidBarrelRecipe.FluidBarrelRecipe;
import com.ist.blastwork.recipe.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIBlastworkPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new FluidBarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FluidBarrelRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipes.FLUID_BARREL_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).toList();


        registration.addRecipes(FluidBarrelRecipeCategory.FLUID_BARREL_RECIPE_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_BARREL.asItem()),
                FluidBarrelRecipeCategory.FLUID_BARREL_RECIPE_RECIPE_TYPE);
    }
}
