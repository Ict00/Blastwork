package com.ist.blastwork.compat.wJei;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.recipe.BlueprintRecipe.BlueprintRecipe;
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

public class BlueprintRecipeCategory implements IRecipeCategory<BlueprintRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, "blueprint_recipe");

    public static final RecipeType<BlueprintRecipe> BLUEPRINT_RECIPE_TYPE =
            new RecipeType<>(UID, BlueprintRecipe.class);
    private final IDrawable icon;

    public BlueprintRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.BLANK_BLUEPRINT.get()));
    }

    @Override
    public RecipeType<BlueprintRecipe> getRecipeType() {
        return BLUEPRINT_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.blastwork.blank_blueprint");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlueprintRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 0).addItemStack(new ItemStack(ModItems.BLANK_BLUEPRINT.get()));


        for (var i : recipe.ingredients()) {
            try {
                Blastwork.LOGGER.debug("{}!!!", i.getItems()[0]);
            }
            catch (Exception ex) {
                Blastwork.LOGGER.debug("{}???", ex.getMessage());
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 15, 20).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(BlueprintRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}
