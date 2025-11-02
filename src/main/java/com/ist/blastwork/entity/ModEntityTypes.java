package com.ist.blastwork.entity;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.item.ImpactExplosiveShell.ImpactExplosiveShellThrown;
import com.ist.blastwork.item.TimedExplosiveShell.TimedExplosiveShellThrown;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Blastwork.MODID);
    public static final Supplier<EntityType<ImpactExplosiveShellThrown>> IMPACT_EXPLOSIVE_SHELL_ET = ENTITY_TYPES.register(
            "impact_explosive_shell", () -> EntityType.Builder.<ImpactExplosiveShellThrown>of(ImpactExplosiveShellThrown::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f).build("impact_explosive_shell"));

    public static final Supplier<EntityType<TimedExplosiveShellThrown>> TIMED_EXPLOSIVE_SHELL_ET = ENTITY_TYPES.register(
            "timed_explosive_shell", () -> EntityType.Builder.<TimedExplosiveShellThrown>of(TimedExplosiveShellThrown::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f).build("timed_explosive_shell"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
