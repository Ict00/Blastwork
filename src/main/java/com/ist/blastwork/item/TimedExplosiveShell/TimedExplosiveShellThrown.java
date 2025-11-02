package com.ist.blastwork.item.TimedExplosiveShell;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class TimedExplosiveShellThrown extends ThrowableItemProjectile {
    public static final EntityDataAccessor<Integer> TIME_TILL_EXPLODED =
            SynchedEntityData.defineId(TimedExplosiveShellThrown.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> STICK =
            SynchedEntityData.defineId(TimedExplosiveShellThrown.class, EntityDataSerializers.BOOLEAN);


    public TimedExplosiveShellThrown(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public TimedExplosiveShellThrown(LivingEntity shooter, Level level) {
        super(ModEntityTypes.TIMED_EXPLOSIVE_SHELL_ET.get(), shooter, level);
    }
    public TimedExplosiveShellThrown(EntityType<? extends TimedExplosiveShellThrown> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if (!getEntityData().get(STICK))
            super.tick();

        int time = getEntityData().get(TIME_TILL_EXPLODED);

        if (time == -1) {
            this.discard();
            return;
        }

        if (time == 0) { Explode(3); }
        time--;
        getEntityData().set(TIME_TILL_EXPLODED, time);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TIME_TILL_EXPLODED, -1);
        builder.define(STICK, false);
    }

    private void Explode(float rad) {
        if (level().isClientSide) return;

        level().explode(
                getOwner(),
                Explosion.getDefaultDamageSource(level(), getOwner()),
                null,
                getBlockX(),
                getBlockY(),
                getBlockZ(),
                rad,
                false,
                Level.ExplosionInteraction.TNT);

        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("time_till_exploded", getEntityData().get(TIME_TILL_EXPLODED));
        compound.putBoolean("stick", getEntityData().get(STICK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        getEntityData().set(TIME_TILL_EXPLODED, compound.getInt("time_till_exploded"));
        getEntityData().set(STICK, compound.getBoolean("stick"));
    }

    @Override
    protected void onHit(HitResult result) {
        getEntityData().set(STICK, true);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.TIMED_EXPLOSIVE_SHELL.get();
    }
}
