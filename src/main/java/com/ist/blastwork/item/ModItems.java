package com.ist.blastwork.item;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockItem;
import com.ist.blastwork.item.Detonator.DetonatorItem;
import com.ist.blastwork.item.FilledExplosiveShell.FilledExplosiveShellItem;
import com.ist.blastwork.item.ImpactExplosiveShell.ImpactExplosiveShellItem;
import com.ist.blastwork.item.TimedExplosiveShell.TimedExplosiveShellItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ist.blastwork.block.ModBlocks.FLUID_BARREL;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Blastwork.MODID);

    public static final DeferredItem<Item> EMPTY_EXPLOSIVE_SHELL = ITEMS.registerSimpleItem("empty_explosive_shell", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> FILLED_EXPLOSIVE_SHELL = ITEMS.register("filled_explosive_shell", () -> new FilledExplosiveShellItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> CREATIVE_FILLED_EXPLOSIVE_SHELL = ITEMS.register("creative_filled_explosive_shell", () -> new FilledExplosiveShellItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> IMPACT_EXPLOSIVE_SHELL = ITEMS.register("impact_explosive_shell", () -> new ImpactExplosiveShellItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> TIMED_EXPLOSIVE_SHELL = ITEMS.register("timed_explosive_shell", () -> new TimedExplosiveShellItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR = ITEMS.register("detonator", () -> new DetonatorItem(new Item.Properties().stacksTo(1).durability(32)));
    public static final DeferredItem<BlockItem> FLUID_BARREL_ITEM = ModItems.ITEMS.register("fluid_barrel", () -> new FluidBarrelBlockItem(FLUID_BARREL.get(), new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
