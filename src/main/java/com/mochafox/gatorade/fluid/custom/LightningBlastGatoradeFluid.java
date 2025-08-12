package com.mochafox.gatorade.fluid.custom;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Fluid representing lightning blast flavored gatorade.
 */
public class LightningBlastGatoradeFluid extends GatoradeFluid.SourceGatoradeFluid {
    public static final String NAME = "lightning_blast_gatorade";

    private static final GatoradeFluid.FluidRegistrySet<LightningBlastGatoradeFluid> REGISTRY_SET = 
        GatoradeFluid.registerFluidSet(NAME, 
            () -> new LightningBlastGatoradeFluid(getProperties()),
            1030,  // kg/m³ - density of chilled Gatorade (higher than water due to sugars/electrolytes)
            1300,  // mPa·s - viscosity of chilled Gatorade (slightly higher than water due to dissolved solids)
            276    // K - temperature of chilled Gatorade (3°C, typical refrigeration temperature)
        );

    public static final DeferredHolder<FluidType, FluidType> TYPE = REGISTRY_SET.type;
    public static final DeferredHolder<Fluid, LightningBlastGatoradeFluid> SOURCE = REGISTRY_SET.source;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> FLOWING = REGISTRY_SET.flowing;
    public static final DeferredBlock<net.minecraft.world.level.block.LiquidBlock> BLOCK = REGISTRY_SET.block;
    public static final DeferredItem<BucketItem> BUCKET = REGISTRY_SET.bucket;

    private static BaseFlowingFluid.Properties getProperties() {
        return REGISTRY_SET.createProperties();
    }

    public LightningBlastGatoradeFluid(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    @Override
    public int getTintColor() {
        return 0xBBffff00; // bright yellow with water-level transparency
    }
}
