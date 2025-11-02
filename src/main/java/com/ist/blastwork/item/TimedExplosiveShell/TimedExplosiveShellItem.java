package com.ist.blastwork.item.TimedExplosiveShell;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.item.ImpactExplosiveShell.ImpactExplosiveShellThrown;
import com.ist.blastwork.item.ModItems;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import static com.ist.blastwork.other.ModData.SET_TIME_ON_EXPLODE_DATA;
import static com.ist.blastwork.other.ModData.TIME_LEFT_TO_EXPLODE_DATA;

public class TimedExplosiveShellItem extends Item {
    public TimedExplosiveShellItem(Properties properties) {
        super(properties);
    }

    private void explode(ItemStack stack, Level level, Entity entity) {
        stack.shrink(1);
        level.explode(
                null,
                Explosion.getDefaultDamageSource(level, entity),
                null,
                entity.getX(),
                entity.getY()+0.5f,
                entity.getZ(),
                3,
                false,
                Level.ExplosionInteraction.TNT);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;

        int fuze = stack.getOrDefault(TIME_LEFT_TO_EXPLODE_DATA.get(), -1);

        if (fuze == 0) {
            explode(stack, level, entity);
            return;
        }

        if (fuze != -1) {
            fuze -= 1;
            int left = stack.getOrDefault(DataComponents.MAX_DAMAGE, fuze) - fuze;
            stack.set(DataComponents.DAMAGE, left);
        }

        stack.set(TIME_LEFT_TO_EXPLODE_DATA.get(), fuze);

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack timedExplosiveStack = player.getItemInHand(usedHand);
        int fuze = timedExplosiveStack.getOrDefault(TIME_LEFT_TO_EXPLODE_DATA.get(), -1);
        timedExplosiveStack.set(TIME_LEFT_TO_EXPLODE_DATA, fuze);

        ItemStack anotherStack;

        if (timedExplosiveStack == player.getMainHandItem()) {
            anotherStack = player.getOffhandItem();
        }
        else {
            anotherStack = player.getMainHandItem();
        }

        if (anotherStack.is(Items.CLOCK) && fuze == -1) {
            // Change set_time_on_explode_data

            int time = timedExplosiveStack.getOrDefault(SET_TIME_ON_EXPLODE_DATA.get(), 120);
            if (player.isCrouching()) {
                if (time > 0) {
                    time -= 20;
                }
            }
            else
                time += 20;

            player.displayClientMessage(Component.translatable("blastwork.time_changed", time/20).withColor(0xfff714), true);

            timedExplosiveStack.set(SET_TIME_ON_EXPLODE_DATA.get(), time);

            return InteractionResultHolder.success(timedExplosiveStack);
        }

        if (anotherStack.is(Items.FLINT_AND_STEEL) && fuze == -1) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!level.isClientSide)
                anotherStack.hurtAndBreak(1, (ServerLevel) level, player, (x) -> { });
            int setFuze = timedExplosiveStack.getOrDefault(SET_TIME_ON_EXPLODE_DATA.get(), 120);
            timedExplosiveStack.set(DataComponents.MAX_DAMAGE, setFuze);
            timedExplosiveStack.set(TIME_LEFT_TO_EXPLODE_DATA.get(), setFuze);

            timedExplosiveStack.set(DataComponents.DAMAGE, 0);

            return InteractionResultHolder.success(timedExplosiveStack);
        }

        if (fuze != -1) {
            // Throw
            level.playSound(
                    null,
                    player.getX(),
                    player.getY()+0.5f,
                    player.getZ(),
                    SoundEvents.SNOWBALL_THROW,
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );

            if (!level.isClientSide) {
                TimedExplosiveShellThrown shell = new TimedExplosiveShellThrown(player, level);
                shell.setItem(new ItemStack(ModItems.TIMED_EXPLOSIVE_SHELL.get()));
                shell.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1f, 1.0F);
                shell.getEntityData().set(TimedExplosiveShellThrown.TIME_TILL_EXPLODED, fuze);
                level.addFreshEntity(shell);


                timedExplosiveStack.shrink(1);
            }

            return InteractionResultHolder.success(timedExplosiveStack);
        }


        return super.use(level, player, usedHand);
    }
}
