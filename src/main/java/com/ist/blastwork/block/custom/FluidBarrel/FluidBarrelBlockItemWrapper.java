package com.ist.blastwork.block.custom.FluidBarrel;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.Config;
import com.ist.blastwork.item.ModItems;
import com.ist.blastwork.other.FluidStackComponent;
import com.ist.blastwork.other.ModData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public class FluidBarrelBlockItemWrapper implements IFluidHandlerItem {
    ItemStack given;

    public FluidBarrelBlockItemWrapper(ItemStack given) {
        this.given = given;
    }

    private static int getCCap() {
        return Config.FLUID_BARREL_CAPACITY.get();
    }

    private FluidStack fluid() {
        FluidStackComponent component = given.getOrDefault(ModData.FLUID_DATA, FluidStackComponent.empty());
        return component.toFluidStack();
    }

    private void setFluid(FluidStack stack) {
        if (stack.getAmount() > getCCap()) {
            stack.setAmount(getCCap());
        }


        given.set(DataComponents.DAMAGE, getCCap() - stack.getAmount() == 0 ? 1 : getCCap() - stack.getAmount());
        given.set(DataComponents.MAX_DAMAGE, getCCap());
        given.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable("%s", stack.getHoverName()).withColor(0xf5e027))));

        if (stack.isEmpty()) {
            given = new ItemStack(ModItems.FLUID_BARREL_ITEM.get());
            return;
        }

        given.set(ModData.FLUID_DATA, new FluidStackComponent(stack));
    }

    @Override
    public ItemStack getContainer() {
        return given;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return getCCap();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        var fluid = fluid();

        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(getCCap(), resource.getAmount());
            }
            if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }
            return Math.min(getCCap() - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = resource.copyWithAmount(Math.min(getCCap(), resource.getAmount()));
            setFluid(fluid);
            return fluid.getAmount();
        }
        if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }
        int filled = getCCap() - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(getCCap());
        }
        setFluid(fluid);
        return filled;
    }

    private boolean isFluidValid(FluidStack resource) {
        return true;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, fluid())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        var fluid = fluid();
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            setFluid(fluid);
        }
        return stack;
    }
}
