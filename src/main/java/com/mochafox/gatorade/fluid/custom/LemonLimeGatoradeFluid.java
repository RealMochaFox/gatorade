package com.mochafox.gatorade.fluid.custom;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Fluid representing lemon-lime flavored gatorade.
 */
public class LemonLimeGatoradeFluid extends GatoradeFluid.SourceGatoradeFluid {
    public static final String NAME = "lemon_lime_gatorade";

    private static final GatoradeFluid.FluidRegistrySet<LemonLimeGatoradeFluid> REGISTRY_SET = 
        GatoradeFluid.registerFluidSet(NAME, 
            () -> new LemonLimeGatoradeFluid(getProperties()),
            1050,
            1200,
            295
        );

    public static final DeferredHolder<FluidType, FluidType> TYPE = REGISTRY_SET.type;
    public static final DeferredHolder<Fluid, LemonLimeGatoradeFluid> SOURCE = REGISTRY_SET.source;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> FLOWING = REGISTRY_SET.flowing;
    public static final DeferredBlock<net.minecraft.world.level.block.LiquidBlock> BLOCK = REGISTRY_SET.block;
    public static final DeferredItem<BucketItem> BUCKET = REGISTRY_SET.bucket;

    private static BaseFlowingFluid.Properties getProperties() {
        return REGISTRY_SET.createProperties();
    }

    public LemonLimeGatoradeFluid(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    @Override
    public int getTintColor() {
        return 0x6600ff00; // green with water-level transparency
    }
}
