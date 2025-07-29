package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.serialization.MapCodec;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public record GatoradeFluidHandlerItemTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<GatoradeFluidHandlerItemTintSource> MAP_CODEC = ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
        .xmap(color -> new GatoradeFluidHandlerItemTintSource(ARGB.opaque(color)), source -> source.defaultColor);

    public GatoradeFluidHandlerItemTintSource() {
        this(ARGB.opaque(Gatorade.DEFAULT_FLUID_COLOR));
    }

    @Override
    public int calculate(@Nonnull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (fluidHandler == null)  {
            return defaultColor;
        }

        Fluid fluid = fluidHandler.getFluidInTank(0).getFluid();
        if (fluid instanceof GatoradeFluid) {
            GatoradeFluid gatoradeFluid = (GatoradeFluid) fluid;
            int tintColor = gatoradeFluid.getTintColor();
            return ARGB.opaque(tintColor);
        }

        return defaultColor;
    }

    @Override
    public MapCodec<GatoradeFluidHandlerItemTintSource> type() {
        return MAP_CODEC;
    }
}