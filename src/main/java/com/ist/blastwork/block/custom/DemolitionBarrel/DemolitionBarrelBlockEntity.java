package com.ist.blastwork.block.custom.DemolitionBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class DemolitionBarrelBlockEntity extends ExplosiveBarrelBlockEntity {

    public DemolitionBarrelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEMOLITION_BARREL_BE.get(), pos, blockState);
    }

    public DemolitionBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEMOLITION_BARREL_BE.get(), pos, blockState);
    }


    public DemolitionBarrelBlockEntity(BlockPos pos, BlockState blockState, int overrideMaxCharge, int overrideFuseOnSetoff) {
        super(ModBlockEntities.DEMOLITION_BARREL_BE.get(), pos, blockState);
        fuzeOnSetoff = overrideFuseOnSetoff;
        maxCharge = overrideMaxCharge;
    }

    public static void staticTick(Level level, BlockPos pos, BlockState state, DemolitionBarrelBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
    }

    @Override
    public void explode() {
        if (level != null && !level.isClientSide) {
            float rad = getCharge()/1.8f;
            BlockPos center = getBlockPos();
            BlockPos min = center.offset((int) -rad, (int) -rad, (int) -rad);
            BlockPos max = center.offset((int) rad, (int) rad, (int) rad);
            RandomSource random = level.getRandom();

            for (BlockPos cur : BlockPos.betweenClosed(min, max)) {
                if (center.distSqr(cur) <= rad * rad) {
                    if (!random.nextBoolean()) continue;
                    if (level.getBlockEntity(cur) != null) continue;
                    if (level.getBlockState(cur).is(BlockTags.AIR)) continue;
                    if (level.getBlockState(cur).is(BlockTags.DRAGON_IMMUNE)) continue;
                    if (level.getBlockState(cur).is(BlockTags.WITHER_IMMUNE)) continue;

                    BlockState state = level.getBlockState(cur);

                    FallingBlockEntity entity = FallingBlockEntity.fall(level, cur, state);
                    level.addFreshEntity(entity);
                }
            }
            setCharge((int) (rad*1.4f));
        }

        super.explode();
    }
}
