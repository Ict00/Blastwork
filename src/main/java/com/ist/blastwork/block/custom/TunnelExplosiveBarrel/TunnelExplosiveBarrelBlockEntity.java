package com.ist.blastwork.block.custom.TunnelExplosiveBarrel;

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


public class TunnelExplosiveBarrelBlockEntity extends ExplosiveBarrelBlockEntity {

    public TunnelExplosiveBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TUNNEL_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }

    public TunnelExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TUNNEL_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }


    public TunnelExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.TUNNEL_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, TunnelExplosiveBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
    }

    @Override
    public void explode() {
        var level = getLevel();
        var pos = getBlockPos();
        assert level != null;

        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, Math.clamp(getCharge()*4, 4f, 20f), (1f - (float) getCharge() / maxCharge));

        int temp = getCharge();
        setCharge(0);
        setChanged();

        if (!level.isClientSide) {
            int changeX = 0, changeY = 0, changeZ = 0;

            BlockState state = level.getBlockState(getBlockPos());

            if (state.hasProperty(BlockStateProperties.FACING)) {
                Direction direction = state.getValue(BlockStateProperties.FACING);
                switch (direction) {
                    case DOWN:
                        changeY = -1;
                        break;
                    case UP:
                        changeY = 1;
                        break;
                    case WEST:
                        changeX = -1;
                        break;
                    case EAST:
                        changeX = 1;
                        break;
                    case NORTH:
                        changeZ = -1;
                        break;
                    case SOUTH:
                        changeZ = 1;
                        break;
                }
            }

            int add = 1;

            float x = pos.getX(), y = pos.getY(), z = pos.getZ();

            for (int i = 1; i <= temp*1.5f; i += add) {
                x = i * changeX;
                y = i * changeY;
                z = i * changeZ;

                level.explode(
                        null,
                        Explosion.getDefaultDamageSource(level, null),
                        null,
                        pos.getX() + x,
                        pos.getY() + y,
                        pos.getZ() + z,
                        2.3f,
                        false,
                        Level.ExplosionInteraction.TNT,
                        ParticleTypes.EXPLOSION,
                        ParticleTypes.EXPLOSION_EMITTER,
                        ModSounds.SOUND_PLACEHOLDER);
            }

            level.removeBlock(pos, false);
        }
    }
}
