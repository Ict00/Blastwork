package com.ist.blastwork.block.custom.Explosive;

import com.ist.blastwork.Blastwork;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementGranter {
    public static void grant(ServerPlayer player, String name) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, name);
        ServerAdvancementManager mgr = player.server.getAdvancements();
        AdvancementHolder advancement = mgr.get(loc);
        for (String crit : player.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
            player.getAdvancements().award(advancement, crit);
        }
    }
}
