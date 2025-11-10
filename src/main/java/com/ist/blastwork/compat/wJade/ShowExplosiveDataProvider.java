package com.ist.blastwork.compat.wJade;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.ExplosiveBarrel.ExplosiveBarrelBlockEntity;
import com.ist.blastwork.block.custom.PotionBarrel.PotionBarrelBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ShowExplosiveDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("Charge") && blockAccessor.getServerData().contains("Special") &&
        blockAccessor.getServerData().contains("Time") && blockAccessor.getServerData().contains("Sealed")) {
            int charge = blockAccessor.getServerData().getInt("Charge");
            int special = blockAccessor.getServerData().getInt("Special");
            int time = blockAccessor.getServerData().getInt("Time");
            boolean isSealed = blockAccessor.getServerData().getBoolean("Sealed");

            iTooltip.add(
                    Component.translatable("jade.blastwork.charge",
                            charge, special == 0 ? Component.literal("") :
                            Component.translatable("jade.blastwork.extra_charge", special)
                                    .withColor(0xd442f5))
            );

            iTooltip.add(
                    Component.translatable("jade.blastwork.time", time/20)
            );

            if (isSealed) {
                iTooltip.add(Component.translatable("blastwork.barrel_sealed"));
            }

            if (blockAccessor.getServerData().contains("Effects")) {
                var effects = PotionBarrelBlockEntity.getEffectsFromTag(blockAccessor.getServerData().get("Effects"));
                if (!effects.isEmpty()) {
                    iTooltip.add(Component.empty());

                    for (var i : effects) {
                        iTooltip.add(Component.translatable("%s", i.value().getDisplayName()).withColor(i.value().getColor()));
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(Blastwork.MODID, "jade_plugin");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        var entity = (ExplosiveBarrelBlockEntity)blockAccessor.getBlockEntity();
        compoundTag.putInt("Charge", entity.getNormalCharge());
        compoundTag.putInt("Special", entity.getSpecialCharge());
        compoundTag.putInt("Time", entity.changeSetoff(0));
        compoundTag.putBoolean("Sealed", entity.isSealed());

        if (entity instanceof PotionBarrelBlockEntity potionEntity) {
            compoundTag.put("Effects", potionEntity.getEffectsTag());
        }
    }
}
