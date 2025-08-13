package com.mochafox.gatorade.fluid.custom;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.ModBlocks;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.item.ModItems;
import com.mochafox.gatorade.item.custom.GatoradeBucketItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
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
     * Helper class for registering a complete set of Gatorade fluid objects.
     */
    public static class FluidRegistrySet<T extends SourceGatoradeFluid> {
        public final DeferredHolder<FluidType, FluidType> type;
        public final DeferredHolder<Fluid, T> source;
        public final DeferredHolder<Fluid, FlowingGatoradeFluid> flowing;
        public final DeferredBlock<LiquidBlock> block;
        public final DeferredItem<BucketItem> bucket;

        private FluidRegistrySet(String name, Supplier<T> sourceFactory, int density, int viscosity, int temperature) {
            this.type = ModFluids.FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .density(density).viscosity(viscosity).temperature(temperature)
                .fallDistanceModifier(0F)
                .canExtinguish(true)
                .canConvertToSource(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
                .addDripstoneDripping(PointedDripstoneBlock.WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_WATER, Blocks.WATER_CAULDRON, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
                    @Override
                    public boolean canConvertToSource(@Nonnull FluidState state, @Nonnull LevelReader reader, @Nonnull BlockPos pos) {
                        if (reader instanceof ServerLevel level) {
                            return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
                        }
                        return super.canConvertToSource(state, reader, pos);
                    }

                    @Override
                    public @Nullable PathType getBlockPathType(@Nonnull FluidState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
                        return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
                    }
                }
            );

            this.source = ModFluids.FLUIDS.register(name, sourceFactory);

            this.flowing = ModFluids.FLUIDS.register(name + "_flowing",
                    () -> new FlowingGatoradeFluid(createLazyProperties()));

            this.block = ModBlocks.BLOCKS.registerBlock(name + "_block",
                    properties -> new LiquidBlock(source.get(), properties
                        .replaceable()
                        .noOcclusion()
                        .sound(SoundType.WET_SPONGE)
                        .mapColor(MapColor.WATER)));

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

        public BaseFlowingFluid.Properties createProperties() {
            return createLazyProperties();
        }
    }

    /**
     * Registers a complete set of fluid objects for a Gatorade flavor.
     * 
     * @param <T>           the source fluid type
     * @param name          the base name for registration
     * @param sourceFactory factory for creating the source fluid instance
     * @param density       fluid density in kg/m³
     * @param viscosity     fluid viscosity in mPa·s
     * @param temperature   fluid temperature in K
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
