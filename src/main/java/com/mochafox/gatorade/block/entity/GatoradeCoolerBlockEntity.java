package com.mochafox.gatorade.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import com.mochafox.gatorade.block.ModBlockEntities;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;
import com.mochafox.gatorade.Config;

/**
 * BlockEntity for the Gatorade Cooler Block that handles fluid storage.
 * Uses Capabilities.FluidHandler.BLOCK for fluid management.
 * Data persistence is handled through data components on the block item.
 */
public class GatoradeCoolerBlockEntity extends BlockEntity {
    private static final int CAPACITY = 16000; // 16 buckets (16,000 mB) capacity
    private FluidStack storedFluid = FluidStack.EMPTY;

    public GatoradeCoolerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GATORADE_COOLER_BLOCK_ENTITY.get(), pos, blockState);
    }

    /**
     * Gets the fluid handler capability for this block entity.
     */
    public IFluidHandler getFluidHandler() {
        return new GatoradeCoolerFluidHandler();
    }

    /**
     * Checks if a fluid is a Gatorade fluid that this cooler can accept
     */
    private boolean isGatoradeFluid(net.minecraft.world.level.material.Fluid fluid) {
        // If chaos mode is enabled, accept any fluid
        if (Config.CHAOS_MODE.get()) {
            return true;
        }
        
        // Check if the fluid is an instance of any GatoradeFluid
        return fluid instanceof GatoradeFluid;
    }

    /**
     * Internal fluid handler implementation for the Gatorade Cooler Block Entity.
     */
    private class GatoradeCoolerFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            if (tank != 0) return FluidStack.EMPTY;
            return storedFluid.copy();
        }

        @Override
        public int getTankCapacity(int tank) {
            if (tank != 0) return 0;
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (tank != 0) return false;
            return isGatoradeFluid(stack.getFluid());
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !isGatoradeFluid(resource.getFluid())) {
                return 0;
            }

            if (storedFluid.isEmpty()) {
                // Empty container - can fill with any gatorade fluid up to capacity
                int fillAmount = Math.min(CAPACITY, resource.getAmount());

                if (action.execute()) {
                    storedFluid = resource.copyWithAmount(fillAmount);
                    setChanged();
                }

                return fillAmount;
            } else {
                // Container has fluid - can only add the same type
                if (FluidStack.isSameFluidSameComponents(storedFluid, resource)) {
                    int fillAmount = Math.min(CAPACITY - storedFluid.getAmount(), resource.getAmount());

                    if (action.execute() && fillAmount > 0) {
                        storedFluid.grow(fillAmount);
                        setChanged();
                    }

                    return fillAmount;
                }

                return 0;
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, storedFluid)) {
                return FluidStack.EMPTY;
            }
            return drain(resource.getAmount(), action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain <= 0) {
                return FluidStack.EMPTY;
            }

            if (storedFluid.isEmpty() || !isGatoradeFluid(storedFluid.getFluid())) {
                return FluidStack.EMPTY;
            }

            final int drainAmount = Math.min(storedFluid.getAmount(), maxDrain);

            FluidStack drained = storedFluid.copyWithAmount(drainAmount);

            if (action.execute()) {
                storedFluid.shrink(drainAmount);
                if (storedFluid.isEmpty()) {
                    storedFluid = FluidStack.EMPTY;
                }
                setChanged();
            }

            return drained;
        }
    }
}
