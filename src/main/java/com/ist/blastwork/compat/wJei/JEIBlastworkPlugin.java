package com.ist.blastwork.compat.wJei;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.recipe.BlueprintRecipe.BlueprintRecipe;
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
import net.minecraft.world.item.Items;
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
        registration.addRecipeCategories(new BlueprintRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FluidBarrelRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipes.FLUID_BARREL_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).toList();

        List<BlueprintRecipe> blueprintRecipes = recipeManager.getAllRecipesFor(ModRecipes.BLUEPRINT_RECIPE_TYPE.get())
                .stream().map(RecipeHolder::value).toList();

        registration.addRecipes(FluidBarrelRecipeCategory.FLUID_BARREL_RECIPE_RECIPE_TYPE, recipes);
        registration.addRecipes(BlueprintRecipeCategory.BLUEPRINT_RECIPE_TYPE, blueprintRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_BARREL.asItem()),
                FluidBarrelRecipeCategory.FLUID_BARREL_RECIPE_RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModItems.QUILL_AND_INK.asItem()),
                BlueprintRecipeCategory.BLUEPRINT_RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModItems.BLANK_BLUEPRINT.asItem()),
                BlueprintRecipeCategory.BLUEPRINT_RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(Items.SMITHING_TABLE.asItem()),
                BlueprintRecipeCategory.BLUEPRINT_RECIPE_TYPE);
    }
}
