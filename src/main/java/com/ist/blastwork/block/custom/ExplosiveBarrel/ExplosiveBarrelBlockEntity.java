package com.ist.blastwork.block.custom.ExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.Explosive.IExplosiveBlock;
import com.ist.blastwork.other.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;


public class ExplosiveBarrelBlockEntity extends BlockEntity implements IExplosiveBlock {
    protected int fuze = -1;
    protected int fuzeOnSetoff = 100;
    protected int maxCharge = 16;
    protected int charge;
    protected int fusesUsed = 0; // max is 8
    protected boolean sealed = false;

    public void setSealed(boolean sealed) {
        this.sealed = sealed;
        setChanged();
    }

    public boolean addFuses() {
        if (fusesUsed >= 8) return false;

        fusesUsed++;
        fuzeOnSetoff += 20;

        return true;
    }

    public boolean isSealed() {
        return sealed;
    }

    public void setCharge(int newCharge) {
        charge = newCharge;
    }

    public int changeSetoff(int change) {
        if (sealed) return fuzeOnSetoff;

        fuzeOnSetoff += change;
        if (fuzeOnSetoff < 0) {
            fuzeOnSetoff = 0;
            setChanged();
            return fuzeOnSetoff;
        }

        setChanged();
        return fuzeOnSetoff;
    }

    public int getCharge() {
        return charge;
    }
    public boolean isEmpty() {return charge == 0; }
    public int getSpecialCharge() {
        if (!reachedMaxCharge()) return 0;
        return charge - maxCharge;
    }
    public int getNormalCharge() {
        if (!reachedMaxCharge()) return getCharge();
        return maxCharge;
    }

    public final boolean reachedMaxCharge() {
        return charge >= maxCharge;
    }
    public final boolean reachedMaxSpecialCharge() {
        return charge >= maxCharge + maxCharge/2;
    }


    public final void tryInsertSpecial(int x) {
        if (sealed) return;
        if (reachedMaxSpecialCharge()) return;
        charge += x;

        if (charge > maxCharge + maxCharge/2) {
            charge = maxCharge + maxCharge/2;
        }
    }

    public final void tryInsert(int x) {
        if (sealed) return;
        if (reachedMaxCharge()) return;
        charge += x;
        if (charge > maxCharge) {
            charge = maxCharge;
        }
        if (charge < 0) {
            charge = 0;
        }
    }

    public ExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.EXPLOSIVE_BARREL_BE.get(), pos, blockState);
    }

    public ExplosiveBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.EXPLOSIVE_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
    }

    public ExplosiveBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("charge", charge);
        tag.putBoolean("sealed", sealed);
        tag.putInt("fuze", fuze);
        tag.putInt("fusesUsed", fuze);
        tag.putInt("fuzeOnSetoff", fuzeOnSetoff);
        tag.putInt("maxCharge", maxCharge);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        charge = tag.getInt("charge");
        sealed = tag.getBoolean("sealed");
        fuze = tag.getInt("fuze");
        fuzeOnSetoff = tag.getInt("fuzeOnSetoff");
        maxCharge = tag.getInt("maxCharge");
        fusesUsed = tag.getInt("fusesUsed");
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, ExplosiveBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (fuze == 0) {
            explode();
            charge = 0;
            setChanged();
        }

        if (fuze != -1) {
            fuze--;
            ((ServerLevel)level).sendParticles(ParticleTypes.SMOKE, pos.getX()+0.5f, pos.getY() + 0.5f, pos.getZ()+0.5f, 10, 0, 0, 0, 0);
            setChanged();
        }
    }
    public boolean setOff() {
        return setOff(0);
    }

    public boolean setOff(int setOffValue) {
        if (fuze != -1) return false;
        if (charge > 0) {
            fuze = fuzeOnSetoff+setOffValue;
            setChanged();
            var pos = getBlockPos();

            if (level != null)
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    public boolean setOn() {
        if (fuze != -1) {
            fuze = -1;
            setChanged();
            return true;
        }
        return false;
    }

    public void explode() {
        var level = getLevel();
        var pos = getBlockPos();

        assert level != null;
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, Math.clamp(getCharge()*4, 4f, 20f), (1f - (float) getCharge() / maxCharge));
        if (level.isClientSide) return;

        int temp = charge;
        charge = 0;
        setChanged();

        level.explode(
                        null,
                    Explosion.getDefaultDamageSource(level, null),
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                0.5F * temp,
                false,
                    Level.ExplosionInteraction.TNT,
                    ParticleTypes.EXPLOSION,
                    ParticleTypes.EXPLOSION_EMITTER,
                    ModSounds.SOUND_PLACEHOLDER);

        level.removeBlock(pos, false);
    }
}
