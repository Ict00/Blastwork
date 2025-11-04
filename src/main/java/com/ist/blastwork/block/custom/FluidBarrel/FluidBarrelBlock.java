package com.ist.blastwork.block.custom.FluidBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.FluidStackComponent;
import com.ist.blastwork.other.ModData;
import com.ist.blastwork.recipe.FluidBarrelRecipe.FluidBarrelRecipeInput;
import com.ist.blastwork.recipe.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidBarrelBlock extends BaseEntityBlock {
    public static final MapCodec<FluidBarrelBlock> CODEC = simpleCodec(FluidBarrelBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public FluidBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidBarrelBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.FLUID_BARREL_BE.get(), FluidBarrelBlockEntity::staticTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof FluidBarrelBlockEntity entity && !level.isClientSide) {
            var cap = entity.getCapability(null);


            IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);


            if (fluidHandler != null && fluidHandler.getTanks() > 0) {
                int fill = fluidHandler.getTankCapacity(0);
                FluidStack fStack = fluidHandler.getFluidInTank(0);

                if (fStack.isEmpty()) {
                    if (!cap.getFluidInTank(0).isEmpty() && fluidHandler.isFluidValid(0, cap.getFluidInTank(0))) {
                        int amount = cap.drain(fill, IFluidHandler.FluidAction.SIMULATE).getAmount();
                        if (amount == fill) {
                            fluidHandler.fill(cap.drain(fill, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                            Fluid fluidToPlay = fluidHandler.getFluidInTank(0).getFluid();

                            if (!player.isCreative())
                                player.setItemInHand(hand, fluidHandler.getContainer());

                            level.playSound(null, pos, fluidToPlay.getPickupSound().isPresent() ? fluidToPlay.getPickupSound().get() : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                            return ItemInteractionResult.SUCCESS;
                        }
                        else {
                            return ItemInteractionResult.FAIL;
                        }
                    }
                }
                else {
                    int res = cap.fill(fStack, IFluidHandler.FluidAction.SIMULATE);
                    if (res == 0) {
                        if (cap.getFluidInTank(0).getAmount() != cap.getTankCapacity(0)) {
                            player.displayClientMessage(Component.translatable("blastwork.fluid_barrel.contains_other", cap.getFluidInTank(0).getHoverName()), true);
                            return ItemInteractionResult.FAIL;
                        }
                        player.displayClientMessage(Component.translatable("blastwork.fluid_barrel.full"), true);
                        return ItemInteractionResult.FAIL;
                    }
                    else {
                        fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        cap.fill(fStack, IFluidHandler.FluidAction.EXECUTE);

                        Fluid fluidToPlay = cap.getFluidInTank(0).getFluid();

                        level.playSound(null, pos, fluidToPlay.getPickupSound().isPresent() ? fluidToPlay.getPickupSound().get() : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);

                        if (!player.isCreative())
                            player.setItemInHand(hand, fluidHandler.getContainer());

                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
            else {
                var input = new FluidBarrelRecipeInput(cap.getFluidInTank(0), stack);
                var optionalRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.FLUID_BARREL_RECIPE_TYPE.get(),
                        input, level);

                if (optionalRecipe.isPresent()) {
                    var actualRecipe = optionalRecipe.get().value();
                    FluidStack drained = cap.drain(actualRecipe.inputFluid().getAmount(), IFluidHandler.FluidAction.SIMULATE);

                    if (drained.getAmount() == actualRecipe.inputFluid().getAmount()) {
                        level.playSound(null, pos, cap.getFluidInTank(0).getFluid().getPickupSound().isPresent() ? cap.getFluidInTank(0).getFluid().getPickupSound().get() : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1, 2);
                        cap.drain(drained.getAmount(), IFluidHandler.FluidAction.EXECUTE);
                        stack.shrink(1);
                        player.getInventory().add(actualRecipe.assemble(input, null));

                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }

        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide && !player.isCreative() && level.getBlockEntity(pos) instanceof FluidBarrelBlockEntity entity) {
            var cap = entity.getCapability(null);
            var stack = new ItemStack(ModItems.FLUID_BARREL_ITEM.get(), 1);

            if(!cap.getFluidInTank(0).isEmpty()) {
                FluidStack fluid1 = cap.getFluidInTank(0);
                stack.set(ModData.FLUID_DATA, new FluidStackComponent(fluid1));
                int max = cap.getTankCapacity(0);
                int amount = cap.getFluidInTank(0).getAmount();
                stack.set(DataComponents.MAX_DAMAGE, max);
                stack.set(DataComponents.LORE, new ItemLore(
                   List.of(Component.translatable("%s", fluid1.getHoverName()).withColor(0xf5e027)))
                );

                stack.set(DataComponents.DAMAGE, max - amount == 0 ? 1 : max - amount);
            }

            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));

        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof FluidBarrelBlockEntity entity && !level.isClientSide) {
            var cap = entity.getCapability(null);
            if(cap.getFluidInTank(0).isEmpty()) {
                player.displayClientMessage(Component.translatable("blastwork.fluid_barrel.empty"), true);
            }
            else {
                player.displayClientMessage(Component.translatable("blastwork.fluid_barrel.info", cap.getFluidInTank(0).getAmount(), cap.getFluidInTank(0).getHoverName()), true);
            }

        }
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }


    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return false;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
}
