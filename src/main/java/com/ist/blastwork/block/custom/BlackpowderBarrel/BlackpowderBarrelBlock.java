package com.ist.blastwork.block.custom.BlackpowderBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.DemolitionBarrel.DemolitionBarrelBlockEntity;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.PotionBarrel.PotionBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlackpowderBarrelBlock extends ExplosiveBarrelBlock {
    public BlackpowderBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlackpowderBarrelBlockEntity(pos, state, 16, 100);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.BLACKPOWDER_BARREL_BE.get(), BlackpowderBarrelBlockEntity::staticTick);
    }
}