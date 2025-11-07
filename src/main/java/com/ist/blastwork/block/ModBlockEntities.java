package com.ist.blastwork.block;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.BlackpowderBarrel.BlackpowderBarrelBlockEntity;
import com.ist.blastwork.block.custom.DemolitionBarrel.DemolitionBarrelBlockEntity;
import com.ist.blastwork.block.custom.DirectedExplosiveBarrel.DirectedExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.DirectedExplosiveBarrel.DirectedExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.FireExplosiveBarrel.FireExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockEntity;
import com.ist.blastwork.block.custom.InstantExplosiveBarrel.InstantExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.PotionBarrel.PotionBarrelBlockEntity;
import com.ist.blastwork.block.custom.TunnelExplosiveBarrel.TunnelExplosiveBarrelBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Blastwork.MODID);

    public static final Supplier<BlockEntityType<ExplosiveBarrelBlockEntity>> EXPLOSIVE_BARREL_BE =
            BLOCK_ENTITIES.register("explosive_barrel_be", () -> BlockEntityType.Builder.of(
                    ExplosiveBarrelBlockEntity::new, ModBlocks.EXPLOSIVE_BARREL.get(), ModBlocks.CREATIVE_EXPLOSIVE_BARREL.get(), ModBlocks.GOLDEN_EXPLOSIVE_BARREL.get()
            ).build(null));
    public static final Supplier<BlockEntityType<DemolitionBarrelBlockEntity>> DEMOLITION_BARREL_BE =
            BLOCK_ENTITIES.register("demolition_barrel_be", () -> BlockEntityType.Builder.of(
                    DemolitionBarrelBlockEntity::new, ModBlocks.DEMOLITION_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<DirectedExplosiveBarrelBlockEntity>> DIRECTED_EXPLOSIVE_BARREL_BE =
            BLOCK_ENTITIES.register("directed_explosive_barrel_be", () -> BlockEntityType.Builder.of(
                    DirectedExplosiveBarrelBlockEntity::new, ModBlocks.DIRECTED_EXPLOSIVE_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<InstantExplosiveBarrelBlockEntity>> INSTANT_EXPLOSIVE_BARREL_BE =
            BLOCK_ENTITIES.register("instant_explosive_barrel_be", () -> BlockEntityType.Builder.of(
                    InstantExplosiveBarrelBlockEntity::new, ModBlocks.INSTANT_EXPLOSIVE_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<FluidBarrelBlockEntity>> FLUID_BARREL_BE =
            BLOCK_ENTITIES.register("fluid_barrel_be", () -> BlockEntityType.Builder.of(
                    FluidBarrelBlockEntity::new, ModBlocks.FLUID_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<FireExplosiveBarrelBlockEntity>> FIRE_EXPLOSIVE_BARREL_BE =
            BLOCK_ENTITIES.register("fire_explosive_barrel_be", () -> BlockEntityType.Builder.of(
                    FireExplosiveBarrelBlockEntity::new, ModBlocks.FIRE_EXPLOSIVE_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<TunnelExplosiveBarrelBlockEntity>> TUNNEL_EXPLOSIVE_BARREL_BE =
            BLOCK_ENTITIES.register("tunnel_explosive_barrel_be", () -> BlockEntityType.Builder.of(
                    TunnelExplosiveBarrelBlockEntity::new, ModBlocks.TUNNEL_EXPLOSIVE_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<BlackpowderBarrelBlockEntity>> BLACKPOWDER_BARREL_BE =
            BLOCK_ENTITIES.register("blackpowder_barrel_be", () -> BlockEntityType.Builder.of(
                    BlackpowderBarrelBlockEntity::new, ModBlocks.BLACKPOWDER_BARREL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<PotionBarrelBlockEntity>> POTION_BARREL_BE =
            BLOCK_ENTITIES.register("potion_barrel_be", () -> BlockEntityType.Builder.of(
                    PotionBarrelBlockEntity::new, ModBlocks.POTION_BARREL.get()
            ).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
