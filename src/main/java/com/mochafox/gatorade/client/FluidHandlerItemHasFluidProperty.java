package com.mochafox.gatorade.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import com.mojang.serialization.MapCodec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public record FluidHandlerItemHasFluidProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<FluidHandlerItemHasFluidProperty> MAP_CODEC = MapCodec.unit(new FluidHandlerItemHasFluidProperty());

    @Override
    public boolean get(@Nonnull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, @Nonnull ItemDisplayContext context) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return !fluidHandler.getFluidInTank(0).isEmpty();
    }

    @Override
    public MapCodec<FluidHandlerItemHasFluidProperty> type() {
        return MAP_CODEC;
    }
}
