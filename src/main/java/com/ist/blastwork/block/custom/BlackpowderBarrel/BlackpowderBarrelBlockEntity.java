package com.ist.blastwork.block.custom.BlackpowderBarrel;

import com.ist.blastwork.Config;
import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.other.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;


public class BlackpowderBarrelBlockEntity extends ExplosiveBarrelBlockEntity {

    public BlackpowderBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BLACKPOWDER_BARREL_BE.get(), pos, blockState);
    }

    public BlackpowderBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BLACKPOWDER_BARREL_BE.get(), pos, blockState);
    }


    public BlackpowderBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.BLACKPOWDER_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, BlackpowderBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
    }

    public static List<Entity> getEntitiesInRadius(Level level, BlockPos center, double radius) {
        AABB searchArea = new AABB(
                center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                center.getX() + radius, center.getY() + radius, center.getZ() + radius
        );

        Predicate<Entity> distancePredicate = entity -> entity.distanceToSqr(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5) <= (radius * radius);

        return level.getEntities((Entity) null, searchArea, distancePredicate);
    }

    @Override
    public void explode() {
        var level = getLevel();
        var pos = getBlockPos();

        assert level != null;
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, Math.clamp(getCharge()*4, 4f, 20f), (1f - (float) getCharge() / maxCharge));
        if (level.isClientSide) return;

        int temp = charge;
        charge = 0;
        setChanged();

        ((ServerLevel)level).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()+0.5f, pos.getY() + 0.5f, pos.getZ()+0.5f, Config.BLACKPOWDER_BARREL_PARTICLE_COUNT.getAsInt(), 2, 2, 2, 0);
        ((ServerLevel)level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX()+0.5f, pos.getY() + 0.5f, pos.getZ()+0.5f, Config.BLACKPOWDER_BARREL_PARTICLE_COUNT.getAsInt(), 2, 2, 2, 0);

        var entities = getEntitiesInRadius(level, pos, temp/2f);

        for (var i : entities) {
            if (i instanceof LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
            }
        }

        level.explode(
                null,
                Explosion.getDefaultDamageSource(level, null),
                calculator,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                0.5F * temp,
                false,
                Level.ExplosionInteraction.TNT,
                ParticleTypes.GUST_EMITTER_LARGE,
                ParticleTypes.GUST_EMITTER_LARGE,
                ModSounds.SOUND_PLACEHOLDER);

        level.removeBlock(pos, false);
    }
}
