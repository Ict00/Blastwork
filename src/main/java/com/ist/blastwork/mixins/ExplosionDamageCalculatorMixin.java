package com.ist.blastwork.mixins;


import com.ist.blastwork.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ExplosionDamageCalculator.class, remap = false)
public class ExplosionDamageCalculatorMixin {

    @Inject(
            method = "getEntityDamageAmount",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetEntityDamageAmount(Explosion explosion, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (Config.USE_NEW_EXPLOSION_DAMAGE_SYSTEM.get()) {
            float radius = explosion.radius() * 2.0F;
            Vec3 center = explosion.center();
            double distance = Math.sqrt(entity.distanceToSqr(center));
            double normalizedDistance = distance / (double)radius;

            double seenPercent = Explosion.getSeenPercent(center, entity);

            if (normalizedDistance >= 1.0) {
                cir.setReturnValue(1.0F);
                cir.cancel();
                return;
            }

            double falloff = Math.pow(1.0 - normalizedDistance, 1.73);

            double effectiveDamage = (falloff * falloff + falloff) / 2.0 * 5.0 * (double)radius + 1.0;

            cir.setReturnValue((float)(seenPercent != 0 ? effectiveDamage/seenPercent : 0));
            cir.cancel();
        }
    }
}
