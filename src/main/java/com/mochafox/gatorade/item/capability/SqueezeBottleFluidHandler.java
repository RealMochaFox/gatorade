package com.mochafox.gatorade.item.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import com.mochafox.gatorade.item.ModDataComponents;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;
import com.mochafox.gatorade.Config;

/**
 * Fluid handler implementation for Squeeze Bottle items.
 */
public class SqueezeBottleFluidHandler implements IFluidHandlerItem {
    private static final int CAPACITY = 1000; // mB capacity

    private ItemStack container;

    public SqueezeBottleFluidHandler(ItemStack container) {
        this.container = container;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }

    /**
     * Gets the fluid currently stored in the bottle
     */
    public FluidStack getFluid() {
        SimpleFluidContent content = container.get(ModDataComponents.SQUEEZE_BOTTLE_FLUID_CONTENT.get());
        if (content == null) {
            return FluidStack.EMPTY;
        }
        return content.copy();
    }

    /**
     * Sets the fluid stored in the bottle
     */
    protected void setFluid(FluidStack fluid) {
        if (fluid.isEmpty()) {
            container.remove(ModDataComponents.SQUEEZE_BOTTLE_FLUID_CONTENT.get());
        } else {
            container.set(ModDataComponents.SQUEEZE_BOTTLE_FLUID_CONTENT.get(), SimpleFluidContent.copyOf(fluid));
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return CAPACITY;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return isGatoradeFluid(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !isGatoradeFluid(resource.getFluid())) {
            return 0;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty()) {
            // Empty container - can fill with any gatorade fluid up to capacity
            int fillAmount = Math.min(CAPACITY, resource.getAmount());

            if (action.execute()) {
                setFluid(resource.copyWithAmount(fillAmount));
            }

            return fillAmount;
        } else {
            // Container has fluid - can only add the same type
            if (FluidStack.isSameFluidSameComponents(contained, resource)) {
                int fillAmount = Math.min(CAPACITY - contained.getAmount(), resource.getAmount());

                if (action.execute() && fillAmount > 0) {
                    contained.grow(fillAmount);
                    setFluid(contained);
                }

                return fillAmount;
            }

            return 0;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, getFluid())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty() || !isGatoradeFluid(contained.getFluid())) {
            return FluidStack.EMPTY;
        }

        final int drainAmount = Math.min(contained.getAmount(), maxDrain);

        FluidStack drained = contained.copyWithAmount(drainAmount);

        if (action.execute()) {
            contained.shrink(drainAmount);
            if (contained.isEmpty()) {
                setFluid(FluidStack.EMPTY);
            } else {
                setFluid(contained);
            }
        }

        return drained;
    }

    /**
     * Checks if a fluid is a Gatorade fluid that this bottle can accept
     */
    private boolean isGatoradeFluid(Fluid fluid) {
        if (Config.CHAOS_MODE.get()) {
            return true;
        }

        return fluid instanceof GatoradeFluid;
    }
}