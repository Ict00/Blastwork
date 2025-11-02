package com.ist.blastwork.block.custom.FireExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.other.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Random;
import org.joml.Vector2f;


public class FireExplosiveBarrelBlockEntity extends ExplosiveBarrelBlockEntity {
    private int fireBlocksCount = 0;


    public FireExplosiveBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIRE_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }

    public FireExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIRE_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }


    public FireExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff, int fireBlocksCount) {
        super(ModBlockEntities.FIRE_EXPLOSIVE_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
        this.fireBlocksCount = fireBlocksCount;
    }

    public void setFireBlocksCount(int set) {
        if (fireBlocksCount == 0) {
            fireBlocksCount = set;
            setChanged();
        }
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, FireExplosiveBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fireBlocksCount = tag.getInt("fireBlocks");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fireBlocks", fireBlocksCount);
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
            BlockPos fireCircleCenter = pos.atY(pos.getY()+1);

            var random = level.getRandom();

            for (int i = 0; i < fireBlocksCount*4; i++) {
                float angle = random.nextInt(360);
                Vector2f relative = getPointsOnCircle(
                        2f, angle);
                Vec3i absolute = new Vec3i((int) (relative.x + fireCircleCenter.getX()),
                        fireCircleCenter.getY(), (int) (relative.y + fireCircleCenter.getZ()));

                BlockState default_fire = Blocks.FIRE.defaultBlockState();

                FallingBlockEntity fallingLava = FallingBlockEntity.fall(level, new BlockPos(absolute), default_fire);

                level.addFreshEntity(fallingLava);
            }


            level.explode(
                    null,
                    Explosion.getDefaultDamageSource(level, null),
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    temp*0.7f,
                    true,
                    Level.ExplosionInteraction.BLOCK,
                    ParticleTypes.EXPLOSION,
                    ParticleTypes.GUST_EMITTER_LARGE,
                    ModSounds.SOUND_PLACEHOLDER);

            level.removeBlock(pos, false);
        }
    }

    private static Vector2f getPointsOnCircle(float r, float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float x = (float) (r * Math.cos(rads));
        float z = (float) (r * Math.sin(rads));

        return new Vector2f(x, z);
    }
}
