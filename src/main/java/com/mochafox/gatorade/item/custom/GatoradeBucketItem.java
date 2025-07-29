package com.mochafox.gatorade.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;

public class GatoradeBucketItem extends BucketItem {
    private final Fluid fluid;

    public GatoradeBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
        this.fluid = fluid;
    }

    @Override
    public Component getName(@Nonnull ItemStack stack) {
        if (fluid != Fluids.EMPTY) {
            return Component.translatable("item.gatorade.gatorade_bucket.name", fluid.getFluidType().getDescription());
        }

        return super.getName(stack);
    }
}
