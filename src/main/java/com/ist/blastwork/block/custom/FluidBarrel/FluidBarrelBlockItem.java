package com.ist.blastwork.block.custom.FluidBarrel;

import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.other.FluidStackComponent;
import com.ist.blastwork.other.ModData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidBarrelBlockItem extends BlockItem {
    public FluidBarrelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        FluidStack containing = context.getItemInHand().getOrDefault(ModData.FLUID_DATA, FluidStackComponent.empty()).toFluidStack();
        var res = super.place(context);

        if (res == InteractionResult.sidedSuccess(context.getLevel().isClientSide)) {
            BlockPos pos = context.getClickedPos();

            if (context.getLevel().getBlockEntity(pos) instanceof FluidBarrelBlockEntity entity) {
                var cap = entity.getCapability(null);
                cap.fill(containing, IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return res;
    }
}
