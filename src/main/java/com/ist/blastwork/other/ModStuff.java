package com.ist.blastwork.other;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModStuff {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Blastwork.MODID);
    public static final Supplier<CreativeModeTab> BLASTWORK_TAB = CREATIVE_MODE_TABS.register("blastwork_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.EXPLOSIVE_BARREL_ITEM.get()))
                    .title(Component.translatable("creativetab.blastwork.blastwork_tab"))
                    .displayItems((x, output) -> {
                        output.accept(ModBlocks.EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModBlocks.DEMOLITION_BARREL_ITEM);
                        output.accept(ModBlocks.BLACKPOWDER_BARREL_ITEM);
                        output.accept(ModBlocks.GOLDEN_EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModBlocks.DIRECTED_EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModBlocks.TUNNEL_EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModBlocks.INSTANT_EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModBlocks.POTION_BARREL_ITEM);
                        output.accept(ModBlocks.CREATIVE_EXPLOSIVE_BARREL_ITEM);
                        output.accept(ModItems.FLUID_BARREL_ITEM);
                        output.accept(ModItems.DETONATOR);
                        output.accept(ModItems.EMPTY_BLAZE_TAG);
                        output.accept(ModItems.BLAZE_TAG);
                        output.accept(ModItems.EMPTY_COPPER_SHELL);
                        output.accept(ModItems.INACTIVE_SHELL);
                        output.accept(ModItems.FIRE_SHELL);
                        output.accept(ModItems.EMPTY_EXPLOSIVE_SHELL);
                        output.accept(ModItems.IMPACT_EXPLOSIVE_SHELL);
                        output.accept(ModItems.OVERHEATED_EXPLOSIVE_SHELL);
                        output.accept(ModItems.TIMED_EXPLOSIVE_SHELL);
                        output.accept(ModItems.SMOLDERING_SHELL);
                        output.accept(ModItems.OVERHEATED_SHELL);
                        output.accept(ModItems.CHARGED_SHELL);
                        output.accept(ModItems.CREATIVE_CHARGED_SHELL);
                        output.accept(Items.GUNPOWDER);
                        output.accept(ModItems.BLAZE_GUNPOWDER);
                        output.accept(ModItems.OVERHEATED_POWDER);
                        output.accept(ModItems.FUSE_ITEM);
                    })
                    .build());


    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
