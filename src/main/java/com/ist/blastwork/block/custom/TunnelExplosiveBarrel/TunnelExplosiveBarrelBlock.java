package com.ist.blastwork.block.custom.TunnelExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TunnelExplosiveBarrelBlock extends ExplosiveBarrelBlock {
    public TunnelExplosiveBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TunnelExplosiveBarrelBlockEntity(pos, state, 32, 100);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.TUNNEL_EXPLOSIVE_BARREL_BE.get(), TunnelExplosiveBarrelBlockEntity::staticTick);
    }
}