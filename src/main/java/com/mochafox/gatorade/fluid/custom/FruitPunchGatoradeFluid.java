package com.mochafox.gatorade.fluid.custom;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Fluid representing fruit punch flavored gatorade.
 */
public class FruitPunchGatoradeFluid extends GatoradeFluid.SourceGatoradeFluid {
    public static final String NAME = "fruit_punch_gatorade";

    private static final GatoradeFluid.FluidRegistrySet<FruitPunchGatoradeFluid> REGISTRY_SET = 
        GatoradeFluid.registerFluidSet(NAME, 
            () -> new FruitPunchGatoradeFluid(getProperties()),
            1050,
            1200,
            295
        );

    public static final DeferredHolder<FluidType, FluidType> TYPE = REGISTRY_SET.type;
    public static final DeferredHolder<Fluid, FruitPunchGatoradeFluid> SOURCE = REGISTRY_SET.source;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> FLOWING = REGISTRY_SET.flowing;
    public static final DeferredBlock<net.minecraft.world.level.block.LiquidBlock> BLOCK = REGISTRY_SET.block;
    public static final DeferredItem<BucketItem> BUCKET = REGISTRY_SET.bucket;

    private static BaseFlowingFluid.Properties getProperties() {
        return REGISTRY_SET.createProperties();
    }

    public FruitPunchGatoradeFluid(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    @Override
    public int getTintColor() {
        return 0x66ff0000; // red with water-level transparency
    }
}
