package com.mochafox.gatorade.client.property;

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

/**
 * Conditional item model property that checks if an item contains fluid.
 * Used to switch between empty and filled item models.
 */
public record HasFluidProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<HasFluidProperty> MAP_CODEC = MapCodec.unit(new HasFluidProperty());

    @Override
    public boolean get(@Nonnull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, @Nonnull ItemDisplayContext context) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return !fluidHandler.getFluidInTank(0).isEmpty();
    }

    @Override
    public MapCodec<HasFluidProperty> type() {
        return MAP_CODEC;
    }
}
