package com.ist.blastwork.item.Detonator;

import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.IExplosiveBlock;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.ModData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetonatorItem extends Item {
    public DetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (itemstack.is(ModItems.DETONATOR)) {
            List<Vector3f> lst = itemstack.getOrDefault(ModData.DETONATOR_TARGET_DATA, List.of());

            if (lst.isEmpty()) {
                if (level.isClientSide) {
                    player.displayClientMessage(Component.translatable("blastwork.detonator.no_targets_linked").withColor(0xFF0000), true);
                    player.playSound(SoundEvents.VAULT_REJECT_REWARDED_PLAYER, 1f, 0.5f);
                }

                return InteractionResultHolder.fail(itemstack);
            }

            if (level.isClientSide) {
                player.playSound(SoundEvents.VAULT_ACTIVATE, 1, 2f);
            }
            int i = 0;

            for (Vector3f dat : lst) {
                var pos = new BlockPos((int) dat.x, (int) dat.y, (int) dat.z);

                if (level.getBlockEntity(pos) instanceof IExplosiveBlock entity) {
                    entity.setOff(i * 10);
                }
                i++;
            }

            if (!level.isClientSide) {
                itemstack.hurtAndBreak(1, (ServerLevel) level, player, (x) -> { });
            }

            itemstack.set(ModData.DETONATOR_TARGET_DATA, List.of());
            return InteractionResultHolder.consume(itemstack);
        }

        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null) {
            if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof IExplosiveBlock) {
                List<Vector3f> lst = context.getItemInHand().getOrDefault(ModData.DETONATOR_TARGET_DATA, List.of());

                ArrayList<Vector3f> newList = getVector3fs(context, lst);
                context.getItemInHand().set(ModData.DETONATOR_TARGET_DATA, newList);

                return InteractionResult.CONSUME;
            }
        }


        return InteractionResult.FAIL;
    }

    private static @NotNull ArrayList<Vector3f> getVector3fs(UseOnContext context, List<Vector3f> lst) {
        Vector3f vecPos = new Vector3f(context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());

        ArrayList<Vector3f> newList = new ArrayList<>(lst);
        if (!newList.contains(vecPos)) {
            if (context.getLevel().isClientSide) {

                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("blastwork.detonator.target_added").withColor(0x00EE00), true);
                    context.getPlayer().playSound(SoundEvents.VAULT_INSERT_ITEM, 1, 2f);
                }
            }
            newList.add(vecPos);
        }
        else {
            if (context.getLevel().isClientSide) {


                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("blastwork.detonator.target_removed").withColor(0xEE0000), true);
                    context.getPlayer().playSound(SoundEvents.VAULT_INSERT_ITEM, 1, 2f);
                }
            }
            newList.remove(vecPos);
        }
        return newList;
    }
}
