package com.ist.blastwork.other;

import com.ist.blastwork.Blastwork;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Blastwork.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_PLACEHOLDER = registerSoundEvent("placeholder");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        var id = ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
