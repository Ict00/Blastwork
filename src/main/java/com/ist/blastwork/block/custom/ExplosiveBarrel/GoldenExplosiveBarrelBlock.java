package com.ist.blastwork.block.custom.ExplosiveBarrel;

import com.ist.blastwork.block.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoldenExplosiveBarrelBlock extends ExplosiveBarrelBlock {
    public GoldenExplosiveBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.CLOCK)) {
            int change = 20;

            if (player.getOffhandItem() == stack) {
                change *= -1;
            }

            if (level.getBlockEntity(pos) instanceof ExplosiveBarrelBlockEntity entity) {
                int newValue = entity.changeSetoff(change);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS, 0.2F, 1.0F);
                player.displayClientMessage(Component.translatable("blastwork.time_changed", newValue/20).withColor(0xfff714), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExplosiveBarrelBlockEntity(pos, state, 48, 100);
    }
}