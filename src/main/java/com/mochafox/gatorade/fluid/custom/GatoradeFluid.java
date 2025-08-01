package com.mochafox.gatorade.fluid.custom;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.ModBlocks;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.item.ModItems;
import com.mochafox.gatorade.item.custom.GatoradeBucketItem;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Base class for all Gatorade fluids.
 * Provides common functionality for both source and flowing variants.
 */
public abstract class GatoradeFluid extends BaseFlowingFluid {
    protected GatoradeFluid(Properties properties) {
        super(properties);
    }

    /**
     * Returns the ARGB color used to tint this fluid.
     */
    public abstract int getTintColor();

    /**
     * Creates a FluidType with the specified physical properties.
     * 
     * @param density the fluid density (kg/m³)
     * @param viscosity the fluid viscosity (mPa·s)
     * @param temperature the fluid temperature (K)
     * @return configured FluidType
     */
    protected static FluidType createFluidType(int density, int viscosity, int temperature) {
        return new FluidType(FluidType.Properties.create()
            .density(density)
            .viscosity(viscosity)
            .temperature(temperature));
    }

    /**
     * Helper class for registering a complete set of Gatorade fluid objects.
     */
    public static class FluidRegistrySet<T extends SourceGatoradeFluid> {
        public final DeferredHolder<FluidType, FluidType> type;
        public final DeferredHolder<Fluid, T> source;
        public final DeferredHolder<Fluid, FlowingGatoradeFluid> flowing;
        public final DeferredBlock<LiquidBlock> block;
        public final DeferredItem<BucketItem> bucket;

        private FluidRegistrySet(String name, Supplier<T> sourceFactory, int density, int viscosity, int temperature) {
            this.type = ModFluids.FLUID_TYPES.register(name, () -> createFluidType(density, viscosity, temperature));
            
            this.source = ModFluids.FLUIDS.register(name, sourceFactory);
            
            this.flowing = ModFluids.FLUIDS.register(name + "_flowing", 
                () -> new FlowingGatoradeFluid(createLazyProperties()));
            
            this.block = ModBlocks.BLOCKS.registerBlock(name + "_block", 
                properties -> new LiquidBlock(source.get(), properties
                    .mapColor(MapColor.WATER)
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()));
            
            this.bucket = ModItems.ITEMS.registerItem(name + "_bucket",
                properties -> new GatoradeBucketItem(source.get(), properties.stacksTo(1)));
        }

        private BaseFlowingFluid.Properties createLazyProperties() {
            Supplier<? extends BaseFlowingFluid> sourceSupplier = () -> (BaseFlowingFluid) source.get();
            Supplier<? extends BaseFlowingFluid> flowingSupplier = () -> flowing.get();
            return new Properties(type, sourceSupplier, flowingSupplier)
                .bucket(() -> bucket.get())
                .block(() -> block.get());
        }

        /**
         * Creates properties for the source fluid in this registry set.
         */
        public BaseFlowingFluid.Properties createProperties() {
            return createLazyProperties();
        }
    }

    /**
     * Registers a complete set of fluid objects for a Gatorade flavor.
     * 
     * @param <T> the source fluid type
     * @param name the base name for registration
     * @param sourceFactory factory for creating the source fluid instance
     * @param density fluid density in kg/m³
     * @param viscosity fluid viscosity in mPa·s
     * @param temperature fluid temperature in K
     * @return a FluidRegistrySet containing all registered objects
     */
    public static <T extends SourceGatoradeFluid> FluidRegistrySet<T> registerFluidSet(
            String name, Supplier<T> sourceFactory, int density, int viscosity, int temperature) {
        return new FluidRegistrySet<>(name, sourceFactory, density, viscosity, temperature);
    }

    /** A flowing variant for custom fluids. */
    public static class FlowingGatoradeFluid extends GatoradeFluid {
        public FlowingGatoradeFluid(Properties properties) {
            super(properties);
        }

        @Override
        protected void createFluidStateDefinition(@Nonnull StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isSource(@Nonnull FluidState state) {
            return false;
        }

        @Override
        public int getAmount(@Nonnull FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public int getTintColor() {
            return Gatorade.DEFAULT_FLUID_COLOR;
        }
    }
 
    /** A source variant for custom fluids. */
    public static class SourceGatoradeFluid extends GatoradeFluid {
        public SourceGatoradeFluid(Properties properties) {
            super(properties);
        }

        @Override
        public boolean isSource(@Nonnull FluidState state) {
            return true;
        }

        @Override
        public int getAmount(@Nonnull FluidState state) {
            return 8;
        }

        @Override
        public int getTintColor() {
            return Gatorade.DEFAULT_FLUID_COLOR;
        }
    }
}
