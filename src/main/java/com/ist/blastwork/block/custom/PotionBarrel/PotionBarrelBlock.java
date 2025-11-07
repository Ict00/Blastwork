package com.ist.blastwork.block.custom.PotionBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.BlackpowderBarrel.BlackpowderBarrelBlockEntity;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionBarrelBlock extends ExplosiveBarrelBlock {

    public PotionBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionBarrelBlockEntity(pos, state, 24, 100);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.POTION_BARREL_BE.get(), PotionBarrelBlockEntity::staticTick);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof PotionItem && level.getBlockEntity(pos) instanceof PotionBarrelBlockEntity entity) {
            if (!entity.isFull()) {
                PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                int used = 0;

                if (contents != null && !level.isClientSide) {
                    for (var i : contents.getAllEffects()) {
                        if (!entity.addEffect(i.getEffect())) {
                            break;
                        }
                        used++;
                    }
                }

                if (used != 0) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                    }

                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 2F);

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}