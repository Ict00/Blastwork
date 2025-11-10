package com.ist.blastwork.block.custom.DemolitioneeringWorkbench;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.ModData;
import com.ist.blastwork.recipe.BlueprintRecipe.BlueprintRecipeInput;
import com.ist.blastwork.recipe.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.Filterable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemolitioneeringWorkbenchBlock extends BaseEntityBlock {
    public static final MapCodec<DemolitioneeringWorkbenchBlock> CODEC = simpleCodec(DemolitioneeringWorkbenchBlock::new);

    public DemolitioneeringWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof DemolitioneeringWorkbenchBlockEntity entity) {
            ItemStack stack;

            for (stack = entity.getCapability(null).extractItem(0, 0, false).copy(); !stack.isEmpty(); stack = entity.getCapability(null).extractItem(0, 0, false).copy()) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                level.addFreshEntity(itemEntity);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if (level.getBlockEntity(pos) instanceof DemolitioneeringWorkbenchBlockEntity entity) {
            var cap = entity.getCapability(null);

            if (cap.getSlots() == 0 && stack.is(Items.STICK)) {
                var recipe = entity.getResource();
                ItemStack bluePrint;
                if (recipe == null) {
                    bluePrint = new ItemStack(ModItems.BLANK_BLUEPRINT.get());
                }
                else {
                    bluePrint = new ItemStack(ModItems.BLUEPRINT.get());
                    bluePrint.set(ModData.BLUEPRINT_RECIPE_CONTAINER, recipe);
                }

                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.getInventory().add(bluePrint);
                }

                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 0.7F, 0.9F);

                level.setBlockAndUpdate(pos, Blocks.SMITHING_TABLE.defaultBlockState());

                return ItemInteractionResult.SUCCESS;
            }
            if (!stack.isEmpty()) {
                if (stack.is(Items.PAPER)) {
                    var recipe = entity.getRecipe();
                    if (recipe != null) {
                        ArrayList<Component> components = new ArrayList<>();
                        var newStack = new ItemStack(Items.PAPER);

                        for (int i = 0; i < recipe.ingredients().size(); i++) {
                            components.add(Component.translatable("item.blastwork.blueprint.paper_id", i+1,
                                    recipe.ingredients().get(i).getItems()[0].getHoverName()).withColor(0xCCCCCC));
                        }

                        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.7F, 1.1F);

                        newStack.set(DataComponents.LORE, new ItemLore(components));
                        newStack.set(DataComponents.ITEM_NAME, Component.translatable("item.blastwork.blueprint.paper"));

                        if (!player.isCreative()) {
                            stack.shrink(1);
                            player.getInventory().add(newStack);
                        }

                        return ItemInteractionResult.SUCCESS;
                    }
                }

                if (stack.is(ModItems.QUILL_AND_INK) && cap.getSlots() == 1 && entity.getRecipe() == null) {
                    var item = entity.extractItem(0, 0, false);
                    var allRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.BLUEPRINT_RECIPE_TYPE.get());

                    for (var i : allRecipes) {
                        if (i.value().getResultItem(null).is(item.getItem())) {
                            if (!level.isClientSide && !player.isCreative()) {
                                stack.hurtAndBreak(1, (ServerLevel) level, player, (x) -> { });
                            }

                            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.7F, 1.1F);

                            entity.setRecipe(i.id(), i.value());
                            if (!level.isClientSide) {
                                ((ServerLevel)level).sendParticles(ParticleTypes.CRIT, pos.getX()+0.5, pos.getY()+0.75, pos.getZ()+0.5, 10, 0.1, 0.1, 0.1, 0.5f);
                            }

                            return ItemInteractionResult.SUCCESS;
                        }
                    }

                    entity.insertItem(0, item, false);
                }


                if (cap.insertItem(0, stack, true).isEmpty()) {
                    var copy = stack.copy();
                    copy.setCount(1);
                    cap.insertItem(0, copy, false);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.7F, 0.9F);

                    return ItemInteractionResult.SUCCESS;
                }
            }
            else {
                var res = cap.extractItem(0, 0, true);
                if (!res.isEmpty()) {
                    cap.extractItem(0, 0, false);
                    if (!player.isCreative()) {
                        player.getInventory().add(res.copy());
                    }

                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.7F, 0.9F);

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            if (level.getBlockEntity(pos) instanceof DemolitioneeringWorkbenchBlockEntity entity) {
                var recipe = entity.getRecipe();

                if (recipe == null) return;

                List<ItemStack> items = new ArrayList<>(entity.getSlots());
                for (int i = 0; i < entity.getSlots(); i++) {
                    items.add(entity.getStackInSlot(i));
                }
                if (recipe.matches(new BlueprintRecipeInput(items), level)) {
                    if (!level.isClientSide) {
                        ((ServerLevel)level).sendParticles(ParticleTypes.CRIT, pos.getX()+0.5, pos.getY()+0.75, pos.getZ()+0.5, 10, 0.1, 0.1, 0.1, 0.5f);
                    }

                    entity.clear();
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.7F, 1.4F);

                    entity.insertItem(0, recipe.getResultItem(null), false);
                }
            }
        }
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DemolitioneeringWorkbenchBlockEntity(pos, state);
    }
}
