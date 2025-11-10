package com.ist.blastwork.item.Blueprint;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.block.custom.DemolitionBarrel.DemolitionBarrelBlockEntity;
import com.ist.blastwork.block.custom.DemolitioneeringWorkbench.DemolitioneeringWorkbenchBlockEntity;
import com.ist.blastwork.other.ModData;
import com.ist.blastwork.recipe.BlueprintRecipe.BlueprintRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class BlueprintItem extends Item {
    public BlueprintItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        if (!context.getLevel().isClientSide) {
            var recipeContainer = context.getItemInHand().get(ModData.BLUEPRINT_RECIPE_CONTAINER);

            var mgr = context.getLevel().getRecipeManager();
            var recipe = mgr.byKey(recipeContainer);
            var pos = context.getClickedPos();

            if (recipe.get().value() instanceof BlueprintRecipe blueprintRecipe) {

                if (context.getLevel().getBlockState(pos).getBlock() == Blocks.SMITHING_TABLE) {

                    var newBlockState = ModBlocks.DEMOLITIONEERING_WORKBENCH.get().defaultBlockState();
                    context.getLevel().setBlockAndUpdate(pos, newBlockState);
                    var entity = (DemolitioneeringWorkbenchBlockEntity)context.getLevel().getBlockEntity(pos);

                    entity.setRecipe(recipeContainer, blueprintRecipe);

                    if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                        context.getItemInHand().shrink(1);
                        context.getPlayer().getInventory().add(new ItemStack(Items.STICK));
                    }

                    context.getLevel().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 0.7F, 0.9F);

                    return InteractionResult.SUCCESS;
                }
            }
        }



        return super.useOn(context);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ModData.BLUEPRINT_RECIPE_CONTAINER) && Minecraft.getInstance().level != null) {
            var recipes = Minecraft.getInstance().level.getRecipeManager();
            var recipeId = stack.get(ModData.BLUEPRINT_RECIPE_CONTAINER);

            var optionalRecipe = recipes.byKey(recipeId);
            if (optionalRecipe.isPresent()) {
                var actualRecipe = optionalRecipe.get().value();
                var res = actualRecipe.getResultItem(null);
                var name = res.getItem().getName(res);

                return Component.translatable("item.blastwork.blueprint_of", name);
            }
        }

        return super.getName(stack);
    }
}
