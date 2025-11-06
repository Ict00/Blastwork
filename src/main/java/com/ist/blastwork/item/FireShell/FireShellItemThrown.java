package com.ist.blastwork.item.FireShell;

import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import net.minecraft.core.BlockPos;
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

public class FireShellItemThrown extends ThrowableItemProjectile {
    public FireShellItemThrown(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public FireShellItemThrown(LivingEntity shooter, Level level) {
        super(ModEntityTypes.FIRE_SHELL_ET.get(), shooter, level);
    }
    public FireShellItemThrown(EntityType<? extends FireShellItemThrown> entityType, Level level) {
        super(entityType, level);
    }

    private void Explode(float rad) {
        if (level().isClientSide) return;

        var random = level().getRandom();

        int max = random.nextInt(10);
        BlockState state = Blocks.FIRE.defaultBlockState();
        BlockPos pos = new BlockPos(getBlockX(), getBlockY(), getBlockZ());

        for (int i = 0; i < max; i++) {
            int x = random.nextInt(-1, 1);
            int z = random.nextInt(-1, 1);
            var entity = FallingBlockEntity.fall(level(), pos.offset(x, 1, z), state);
            level().addFreshEntity(entity);
        }

        level().explode(
                null,
                Explosion.getDefaultDamageSource(level(), getOwner()),
                null,
                getBlockX(),
                getBlockY(),
                getBlockZ(),
                rad,
                true,
                Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Explode(1.5f);

        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.FIRE_SHELL.get();
    }
}
