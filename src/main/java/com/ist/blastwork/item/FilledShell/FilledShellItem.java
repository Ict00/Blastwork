package com.ist.blastwork.item.FilledShell;

import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.block.custom.FireExplosiveBarrel.FireExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.Tags;

public class FilledShellItem extends Item {

    public FilledShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();

        if (level.getBlockEntity(context.getClickedPos()) instanceof FluidBarrelBlockEntity entity) {
            if (entity.getCapability(null).getFluidInTank(0).is(Tags.Fluids.LAVA)) {
                int fireAmount = entity.getCapability(null).getFluidInTank(0).getAmount()/500;
                if (!context.getLevel().isClientSide) {

                    Direction facing = level.getBlockState(pos).getValue(BlockStateProperties.FACING);
                    var newState = ModBlocks.FIRE_EXPLOSIVE_BARREL.get().defaultBlockState();
                    newState = newState.setValue(BlockStateProperties.FACING, facing);

                    level.setBlockAndUpdate(pos, newState);
                    ((ServerLevel)level).sendParticles(ParticleTypes.ASH,
                            (double) pos.getX(), (double) pos.getY()+0.5, (double) pos.getZ(), 20, (double) 0, (double) 0, (double) 0, (double) 0);
                    if (level.getBlockEntity(pos) instanceof FireExplosiveBarrelBlockEntity newEntity) {
                        newEntity.setFireBlocksCount(fireAmount);
                    }
                    context.getItemInHand().shrink(1);
                }



                level.playSound(null, pos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 1, 2);

                return InteractionResult.SUCCESS;
            }

        }
        return super.useOn(context);
    }
}
