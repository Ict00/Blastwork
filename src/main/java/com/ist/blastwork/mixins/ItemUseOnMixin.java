package com.ist.blastwork.mixins;


import com.ist.blastwork.Config;
import com.ist.blastwork.block.custom.ExplosiveBarrel.GoldenExplosiveBarrelBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, remap = false)
public class ItemUseOnMixin {
    @Inject(
            method = "useOn",
            at = @At("TAIL"),
            cancellable = true)
    private void onUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getItemInHand().is(Items.CLOCK)) {
            if (context.getPlayer() != null && context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof GoldenExplosiveBarrelBlock block) {

                if (context.getPlayer().isCrouching()) {
                    block.setChange(context.getLevel(), context.getClickedPos(), context.getPlayer(), -20);
                }
                else {
                    block.setChange(context.getLevel(), context.getClickedPos(), context.getPlayer(), 20);
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
            }
        }
    }
}
