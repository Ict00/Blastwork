package com.ist.blastwork;

import com.ist.blastwork.block.ModBlocks;
import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockEntity;
import com.ist.blastwork.block.custom.FluidBarrel.FluidBarrelBlockItemWrapper;
import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.ModData;
import com.ist.blastwork.other.ModDataMaps;
import com.ist.blastwork.other.ModSounds;
import com.ist.blastwork.other.ModStuff;
import com.ist.blastwork.recipe.ModRecipes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Blastwork.MODID)
public class Blastwork {
    public static final String MODID = "blastwork";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Blastwork(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);


        ModBlocks.register(modEventBus);
        ModSounds.register(modEventBus);
        ModData.register(modEventBus);
        ModStuff.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::registerCapabilityProvider);
        modEventBus.addListener(this::registerDataMapTypes);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
    }

    private void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ModDataMaps.CHARGES_MAP);
    }


    public void registerCapabilityProvider(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.FLUID_BARREL_BE.get(),
                FluidBarrelBlockEntity::getCapability
        );
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (itemStack, context) -> new FluidBarrelBlockItemWrapper(itemStack),
                ModItems.FLUID_BARREL_ITEM.get());
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }
}
