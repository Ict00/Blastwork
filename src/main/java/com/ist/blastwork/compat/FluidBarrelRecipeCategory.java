package com.ist.blastwork.compat;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.recipe.FluidBarrelRecipe.FluidBarrelRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FluidBarrelRecipeCategory implements IRecipeCategory<FluidBarrelRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, "fluid_barrel_recipe");

    public static final RecipeType<FluidBarrelRecipe> FLUID_BARREL_RECIPE_RECIPE_TYPE =
            new RecipeType<>(UID, FluidBarrelRecipe.class);
    private final IDrawable icon;

    public FluidBarrelRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.FLUID_BARREL_ITEM.get()));
    }

    @Override
    public RecipeType<FluidBarrelRecipe> getRecipeType() {
        return FLUID_BARREL_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.blastwork.fluid_barrel");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public int getWidth() {
        return 70;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidBarrelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 15).addFluidStack(recipe.inputFluid().getFluid(), recipe.inputFluid().getAmount());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 15).addItemStack(recipe.getResultItem(null));
    }
}
