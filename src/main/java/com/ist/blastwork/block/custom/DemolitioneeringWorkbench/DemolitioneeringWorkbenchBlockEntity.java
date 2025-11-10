package com.ist.blastwork.block.custom.DemolitioneeringWorkbench;

import com.ist.blastwork.block.ModBlockEntities;
import com.ist.blastwork.recipe.BlueprintRecipe.BlueprintRecipe;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemolitioneeringWorkbenchBlockEntity extends BlockEntity implements IItemHandler {
    private ArrayList<ItemStack> allItems;
    private BlueprintRecipe recipe;
    private ResourceLocation recipeLocation;
    private static float rot;

    public static float getRot() {
        rot += 0.5f;
        if (rot >= 360) {
            rot = 0;
        }
        return rot;
    }

    public DemolitioneeringWorkbenchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public DemolitioneeringWorkbenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEMOLITIONEERING_WORKBENCH_BE.get(), pos, blockState);
        allItems = new ArrayList<>();
    }

    private static ResourceLocation getResourceFrom(Tag tag) {
        return ResourceLocation.CODEC.parse(NbtOps.INSTANCE, tag).resultOrPartial(x -> { }).orElse(null);
    }

    private Tag getResourcesTag() {
        return ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, recipeLocation).getOrThrow();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        try {
            tag.put("items", Codec.list(ItemStack.CODEC).encodeStart(NbtOps.INSTANCE, allItems).getOrThrow());
            tag.put("recipeLocation", ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, recipeLocation).getOrThrow());
            tag.put("recipe", BlueprintRecipe.CODEC.encodeStart(NbtOps.INSTANCE, recipe).getOrThrow());
        }
        catch (Exception e) { }
    }

    public ResourceLocation getResource() {
        return recipeLocation;
    }

    public BlueprintRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ResourceLocation loc, BlueprintRecipe recipe) {
        this.recipe = recipe;
        this.recipeLocation = loc;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        try {
            allItems = new ArrayList<>(Codec.list(ItemStack.CODEC).parse(NbtOps.INSTANCE, tag.get("items")).resultOrPartial(x -> {
            }).orElse(List.of()));
            recipeLocation = ResourceLocation.CODEC.parse(NbtOps.INSTANCE, tag.get("recipeLocation")).getOrThrow();
            recipe = (BlueprintRecipe) BlueprintRecipe.CODEC.parse(NbtOps.INSTANCE, tag.get("recipe")).resultOrPartial(x -> {
            }).orElse(null);
        }
        catch (Exception ex) { }
    }

    public @Nullable IItemHandler getCapability(@Nullable Direction direction) {
        return this;
    }

    @Override
    public int getSlots() {
        return allItems.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot >= 0 && slot < allItems.size()) return allItems.get(slot);

        return null;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

        if (allItems.size() >= 10) return stack;

        if (!simulate) {
            allItems.add(stack);
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (allItems.isEmpty()) return ItemStack.EMPTY;
        var stack = allItems.getLast();

        if (!simulate) {
            allItems.removeLast();
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }

        return stack;
    }

    public void clear() {
        allItems.clear();
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}