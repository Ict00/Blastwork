package com.ist.blastwork.block.custom.ExplosiveBarrel;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.Explosive.GunpowderCharge;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExplosiveBarrelBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final MapCodec<ExplosiveBarrelBlock> CODEC = simpleCodec(ExplosiveBarrelBlock::new);
    protected boolean dropGunpowderOnBroken = true;

    public ExplosiveBarrelBlock(Properties properties) {
        super(properties);
    }

    void dropContents(Level level, BlockPos pos) {
        if (!level.isClientSide)
            if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
                if (entity.getCharge() > 0) {
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GUNPOWDER, entity.getCharge())));
                }
            }
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
            entity.explode();
        }

        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (dropGunpowderOnBroken)
            dropContents(level, pos);

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExplosiveBarrelBlockEntity(pos, state);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
                if(entity.setOff())
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos)) {
                if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
                    if(entity.setOff())
                        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
            if (!stack.isEmpty()) {
                if (stack.is(Tags.Items.TOOLS_IGNITER)) {
                    if(entity.setOff()) {
                        if (!level.isClientSide && stack.isDamageableItem() && !player.isCreative())
                            stack.hurtAndBreak(1, (ServerLevel) level, player, (x) -> { });
                        return ItemInteractionResult.SUCCESS;
                    }
                    else {
                        return ItemInteractionResult.FAIL;
                    }
                }
                else if (GunpowderCharge.getCharge(stack) != 0) {
                    int insert = GunpowderCharge.getCharge(stack);

                    if (entity.reachedMaxCharge()) {
                        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        player.displayClientMessage(Component.translatable("blastwork.reached_max_charge", entity.maxCharge).withColor(0xFF0000), true);
                        return ItemInteractionResult.FAIL;
                    }

                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6F, 1.0F);
                    entity.tryInsert(insert);
                    if (!player.isCreative())
                        stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }

            }
        }
        return ItemInteractionResult.FAIL;
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.EXPLOSIVE_BARREL_BE.get(), ExplosiveBarrelBlockEntity::staticTick);
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
