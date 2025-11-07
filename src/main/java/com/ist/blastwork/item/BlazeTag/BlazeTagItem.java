package com.ist.blastwork.item.BlazeTag;

import com.ist.blastwork.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class BlazeTagItem extends Item {
    public BlazeTagItem(Properties properties) {
        super(properties);
    }

    private void explode(LivingEntity attacker, Level level, double x, double y, double z, float rad) {
        level.explode(
                attacker,
                Explosion.getDefaultDamageSource(level, attacker),
                null,
                x,
                y,
                z,
                rad,
                true,
                Level.ExplosionInteraction.MOB);
    }

    private void damage(ItemStack stack, LivingEntity entity, ServerLevel level) {
        stack.hurtAndBreak(1, level, entity, (x) -> {
            if (entity instanceof Player player) {
                player.getInventory().add(new ItemStack(ModItems.EMPTY_BLAZE_TAG.get()));
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getItemInHand().is(ModItems.BLAZE_TAG)) {
            if (!context.getLevel().isClientSide) {
                if (context.getPlayer() != null && context.getPlayer().isCrouching()) {
                    damage(context.getItemInHand(), context.getPlayer(), (ServerLevel)context.getLevel());

                    explode(context.getPlayer(), context.getLevel(),
                            context.getClickedPos().getX(),
                            context.getClickedPos().getY(),
                            context.getClickedPos().getZ(), 1.5f);

                    context.getPlayer().getCooldowns().addCooldown(context.getItemInHand().getItem(), 20);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.is(ModItems.BLAZE_TAG)) {
            if (!target.level().isClientSide) {
                damage(stack, attacker, (ServerLevel) target.level());

                explode(attacker, target.level(), target.position().x, target.position().y, target.position().z, 2);

                ((ServerLevel)target.level()).sendParticles(ParticleTypes.LAVA, target.position().x, target.position().y, target.position().z,30, 0.5, 1, 0.5, 4);
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}
