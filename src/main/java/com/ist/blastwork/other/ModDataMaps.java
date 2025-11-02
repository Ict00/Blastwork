package com.ist.blastwork.other;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.Explosive.GunpowderCharge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class ModDataMaps {
    public static final DataMapType<Item, GunpowderCharge> CHARGES_MAP =
            DataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, "gunpowder_charges"),
                    Registries.ITEM,
                    GunpowderCharge.CODEC
            ).build();
}
