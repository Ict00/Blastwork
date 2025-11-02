package com.ist.blastwork.block.custom.Explosive;

import com.ist.blastwork.other.ModDataMaps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record GunpowderCharge(int charge, boolean special) {
    public static final Codec<GunpowderCharge> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(Codec.INT.fieldOf("charge").forGetter(GunpowderCharge::charge),
                                    Codec.BOOL.optionalFieldOf("special", false).forGetter(GunpowderCharge::special))
                            .apply(instance, GunpowderCharge::new));

    public static int getCharge(ItemStack stack) {
        Holder<Item> holder = stack.getItemHolder();
        GunpowderCharge data = holder.getData(ModDataMaps.CHARGES_MAP);
        return data != null ? data.charge : 0;
    }

    public static boolean isSpecial(ItemStack stack) {
        Holder<Item> holder = stack.getItemHolder();
        GunpowderCharge data = holder.getData(ModDataMaps.CHARGES_MAP);
        return data != null ? data.special : false;
    }
}
