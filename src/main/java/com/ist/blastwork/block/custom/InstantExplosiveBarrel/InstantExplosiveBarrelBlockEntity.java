package com.ist.blastwork.block.custom.InstantExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.other.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class InstantExplosiveBarrelBlockEntity extends ExplosiveBarrelBlockEntity {

    public InstantExplosiveBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.INSTANT_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }

    public InstantExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.INSTANT_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }


    public InstantExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.INSTANT_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
        charge = 8;
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, InstantExplosiveBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    public boolean setOff() {
        return super.setOff();
    }

    @Override
    public boolean setOff(int setOffValue) {
        if (fuze != -1) return false;
        fuze = setOffValue;
        var pos = getBlockPos();

        if (level != null)
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
    }
}
