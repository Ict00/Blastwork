package com.ist.blastwork.block.custom.FireExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.DirectedExplosiveBarrel.DirectedExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FireExplosiveBarrelBlock extends ExplosiveBarrelBlock {
    public FireExplosiveBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FireExplosiveBarrelBlockEntity(pos, state, 16, 100, 0);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.FIRE_EXPLOSIVE_BARREL_BE.get(), FireExplosiveBarrelBlockEntity::staticTick);
    }
}