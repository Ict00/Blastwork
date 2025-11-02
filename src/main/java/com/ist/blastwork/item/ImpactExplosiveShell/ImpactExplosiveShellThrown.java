package com.ist.blastwork.item.ImpactExplosiveShell;

import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ImpactExplosiveShellThrown extends ThrowableItemProjectile {
    public ImpactExplosiveShellThrown(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public ImpactExplosiveShellThrown(LivingEntity shooter, Level level) {
        super(ModEntityTypes.IMPACT_EXPLOSIVE_SHELL_ET.get(), shooter, level);
    }
    public ImpactExplosiveShellThrown(EntityType<? extends ImpactExplosiveShellThrown> entityType, Level level) {
        super(entityType, level);
    }

    private void Explode(float rad) {
        if (level().isClientSide) return;

        level().explode(
                null,
                Explosion.getDefaultDamageSource(level(), getOwner()),
                null,
                getBlockX(),
                getBlockY(),
                getBlockZ(),
                rad,
                false,
                Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Explode(3);

        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.IMPACT_EXPLOSIVE_SHELL.get();
    }
}
