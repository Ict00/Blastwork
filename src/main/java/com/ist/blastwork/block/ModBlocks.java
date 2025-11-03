package com.ist.blastwork.block;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.DemolitionBarrel.DemolitionBarrelBlock;
import com.ist.blastwork.block.custom.DirectedExplosiveBarrel.DirectedExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.ExplosiveBarrel.GoldenExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.FireExplosiveBarrel.FireExplosiveBarrelBlock;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlock;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockItem;
import com.ist.blastwork.block.custom.InstantExplosiveBarrel.InstantExplosiveBarrelBlock;
import com.ist.blastwork.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Blastwork.MODID);

    public static final DeferredBlock<Block> EXPLOSIVE_BARREL = BLOCKS.register("explosive_barrel", () -> new ExplosiveBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> GOLDEN_EXPLOSIVE_BARREL = BLOCKS.register("golden_explosive_barrel", () -> new GoldenExplosiveBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> DIRECTED_EXPLOSIVE_BARREL = BLOCKS.register("directed_explosive_barrel", () -> new DirectedExplosiveBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> FIRE_EXPLOSIVE_BARREL = BLOCKS.register("fire_explosive_barrel", () -> new FireExplosiveBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> INSTANT_EXPLOSIVE_BARREL = BLOCKS.register("instant_explosive_barrel", () -> new InstantExplosiveBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> FLUID_BARREL = BLOCKS.register("fluid_barrel", () -> new FluidBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredBlock<Block> DEMOLITION_BARREL = BLOCKS.register("demolition_barrel", () -> new DemolitionBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final DeferredItem<BlockItem> EXPLOSIVE_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("explosive_barrel", EXPLOSIVE_BARREL);
    public static final DeferredItem<BlockItem> GOLDEN_EXPLOSIVE_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("golden_explosive_barrel", GOLDEN_EXPLOSIVE_BARREL);
    public static final DeferredItem<BlockItem> DIRECTED_EXPLOSIVE_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("directed_explosive_barrel", DIRECTED_EXPLOSIVE_BARREL);
    public static final DeferredItem<BlockItem> INSTANT_EXPLOSIVE_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("instant_explosive_barrel", INSTANT_EXPLOSIVE_BARREL);
    public static final DeferredItem<BlockItem> DEMOLITION_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("demolition_barrel", DEMOLITION_BARREL);
    public static final DeferredItem<BlockItem> FIRE_EXPLOSIVE_BARREL_ITEM = ModItems.ITEMS.registerSimpleBlockItem("fire_explosive_barrel", FIRE_EXPLOSIVE_BARREL);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
