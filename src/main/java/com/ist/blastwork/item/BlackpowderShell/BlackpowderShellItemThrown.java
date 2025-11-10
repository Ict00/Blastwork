package com.ist.blastwork.item.BlackpowderShell;

import com.ist.blastwork.Config;
import com.ist.blastwork.block.custom.BlackpowderBarrel.BlackpowderBarrelBlockEntity;
import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class BlackpowderShellItemThrown extends ThrowableItemProjectile {
    public BlackpowderShellItemThrown(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public BlackpowderShellItemThrown(LivingEntity shooter, Level level) {
        super(ModEntityTypes.BLACKPOWDER_SHELL_ET.get(), shooter, level);
    }
    public BlackpowderShellItemThrown(EntityType<? extends BlackpowderShellItemThrown> entityType, Level level) {
        super(entityType, level);
    }

    private void Explode(float rad) {
        if (level().isClientSide) return;

        var level = level();
        var pos = position();

        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4, 0.5f);
        if (level.isClientSide) return;


        ((ServerLevel)level).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.x()+0.5f, pos.y() + 0.5f, pos.z()+0.5f, Config.BLACKPOWDER_BARREL_PARTICLE_COUNT.getAsInt(), 2, 2, 2, 0);
        ((ServerLevel)level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x()+0.5f, pos.y() + 0.5f, pos.z()+0.5f, Config.BLACKPOWDER_BARREL_PARTICLE_COUNT.getAsInt(), 2, 2, 2, 0);

        var entities = BlackpowderBarrelBlockEntity.getEntitiesInRadius(level, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z), rad);

        for (var i : entities) {
            if (i instanceof LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
            }
        }

        level.explode(
                null,
                Explosion.getDefaultDamageSource(level, null),
                null,
                pos.x,
                pos.y,
                pos.z,
                rad,
                false,
                Level.ExplosionInteraction.TNT,
                ParticleTypes.GUST_EMITTER_LARGE,
                ParticleTypes.GUST_EMITTER_LARGE,
                ModSounds.SOUND_PLACEHOLDER);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Explode(3f);

        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.BLACKPOWDER_EXPLOSIVE_SHELL.get();
    }
}
