package com.ist.blastwork.block.custom.ExplosiveBarrel;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeExplosiveBarrelBlock extends GoldenExplosiveBarrelBlock {
    public CreativeExplosiveBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExplosiveBarrelBlockEntity(pos, state, 512, 100);
    }
}