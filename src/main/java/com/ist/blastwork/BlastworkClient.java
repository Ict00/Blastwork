package com.ist.blastwork;

import com.ist.blastwork.entity.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Blastwork.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Blastwork.MODID, value = Dist.CLIENT)
public class BlastworkClient {
    public BlastworkClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntityTypes.IMPACT_EXPLOSIVE_SHELL_ET.get(),
                ThrownItemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntityTypes.FIRE_SHELL_ET.get(),
                ThrownItemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntityTypes.OVERHEATED_EXPLOSIVE_SHELL_ET.get(),
                ThrownItemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntityTypes.TIMED_EXPLOSIVE_SHELL_ET.get(),
                ThrownItemRenderer::new
        );
    }
}
