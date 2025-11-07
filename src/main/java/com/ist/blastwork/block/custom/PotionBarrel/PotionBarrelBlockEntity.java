package com.ist.blastwork.block.custom.PotionBarrel;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.Config;
import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.BlackpowderBarrel.BlackpowderBarrelBlockEntity;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.other.ModSounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class PotionBarrelBlockEntity extends ExplosiveBarrelBlockEntity {
    ArrayList<Holder<MobEffect>> effects = new ArrayList<>(); // 5 is max
    public static final Codec<List<Holder<MobEffect>>> CODEC = Codec.list(MobEffect.CODEC);

    public PotionBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POTION_BARREL_BE.get(), pos, blockState);
    }

    public PotionBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POTION_BARREL_BE.get(), pos, blockState);
    }


    public PotionBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.POTION_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("effects", getEffectsTag());
    }

    public static List<Holder<MobEffect>> getEffectsFromTag(Tag tag) {
        return CODEC.parse(NbtOps.INSTANCE, tag).resultOrPartial(x -> { }).orElse(List.of());
    }

    public Tag getEffectsTag() {
        return CODEC.encodeStart(NbtOps.INSTANCE, effects).getOrThrow();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        effects = new ArrayList<>(CODEC.parse(NbtOps.INSTANCE, tag.get("effects")).resultOrPartial(x -> { }).orElse(new ArrayList<>()));
    }

    public List<Holder<MobEffect>> getEffects() {
        return effects;
    }

    public boolean isFull() {
        return effects.size() >= 5;
    }

    public boolean addEffect(Holder<MobEffect> instance) {
        if (effects.contains(instance)) return false;
        if (effects.size() < 5) {
            effects.add(instance);
            return true;
        }

        return false;
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, PotionBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
    }

    @Override
    public void explode() {

        if (level != null && !level.isClientSide && !effects.isEmpty()) {
            var pos = getBlockPos();
            var charge = getCharge();
            var all = BlackpowderBarrelBlockEntity.getEntitiesInRadius(level, pos, charge);
            var random = level.getRandom();
            var bound = effects.size();

            if (bound <= 0) {
                bound = 1;
            }

            for (var i : all) {
                if (i instanceof LivingEntity entity) {
                    var effect = effects.get(random.nextInt(bound));
                    entity.addEffect(new MobEffectInstance(effect, 600));
                }
            }
        }

        super.explode();
    }
}
