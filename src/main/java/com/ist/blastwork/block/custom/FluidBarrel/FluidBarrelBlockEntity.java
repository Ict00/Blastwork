package com.ist.blastwork.block.custom.FluidBarrel;

import com.ist.blastwork.Config;
import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.Explosive.AdvancementGranter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class FluidBarrelBlockEntity extends BlockEntity {
    private final BlockCapabilityCache<IFluidHandler, Direction>[] capabilityCaches = new BlockCapabilityCache[6];

    protected FluidTank tank = new FluidTank(Config.FLUID_BARREL_CAPACITY.get()) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                if (level.dimensionType().ultraWarm() && tank.getFluid().is(Tags.Fluids.WATER)) {
                    var pos = getBlockPos();
                    var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, null);

                    if (player instanceof ServerPlayer serverPlayer) {
                        AdvancementGranter.grant(serverPlayer, "heat_proof");
                    }
                }
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public FluidBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FLUID_BARREL_BE.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tank.writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag);
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, FluidBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!Config.FLUID_BARREL_FLUID_MOVEMENT.get()) return;

        var belowPos = pos.atY(pos.getY()-1);

        if (level.getBlockEntity(pos) instanceof FluidBarrelBlockEntity thisEntity && level.getBlockEntity(belowPos) instanceof FluidBarrelBlockEntity belowEntity) {
            if (state.getValue(BlockStateProperties.FACING) == Direction.UP
                    && level.getBlockState(belowPos).getValue(BlockStateProperties.FACING) == Direction.UP) {
                var thisFluidCap = thisEntity.getCapability(null);
                var belowFluidCap = belowEntity.getCapability(null);


                FluidStack drain = thisFluidCap.drain(thisFluidCap.getTankCapacity(0), IFluidHandler.FluidAction.SIMULATE);

                if (!drain.isEmpty()) {
                    int fill = belowFluidCap.fill(drain, IFluidHandler.FluidAction.SIMULATE);
                    if (fill != 0) {
                        // Success, move

                        thisFluidCap.drain(drain, IFluidHandler.FluidAction.EXECUTE);
                        int filled = belowFluidCap.fill(drain, IFluidHandler.FluidAction.EXECUTE);
                        thisFluidCap.fill(new FluidStack(drain.getFluid(), drain.getAmount() - filled), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    private void onNeighborCapabilityInvalidated() {
        setChanged();
        invalidateCapabilities();
    }

    public IFluidHandler getCapability(@Nullable Direction side) {
        return tank;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            for (Direction direction : Direction.values()) {
                capabilityCaches[direction.ordinal()] = BlockCapabilityCache.create(
                    Capabilities.FluidHandler.BLOCK,
                        (ServerLevel) level,
                        worldPosition.relative(direction),
                        direction.getOpposite(),
                        () -> !this.isRemoved(),
                        this::onNeighborCapabilityInvalidated
                );
            }
            invalidateCapabilities();
        }
    }
}
