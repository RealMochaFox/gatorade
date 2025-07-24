package com.mochafox.gatorade.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.Registries;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.custom.ArcticBlitzGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.BlueCherryGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.CoolBlueGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.FruitPunchGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GlacierCherryGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GlacierFreezeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GrapeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GreenAppleGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LemonLimeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LightningBlastGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LimeCucumberGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.MidnightIceGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.OrangeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.StrawberryGatoradeFluid;

/**
 * Registry class for all mod fluids and fluid types.
 */
public class ModFluids {
    // Deferred registers
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Gatorade.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Gatorade.MODID);

    // Fluids
    /// Mainline Gatorade fluids
    public static final DeferredHolder<FluidType, FluidType> FRUIT_PUNCH_GATORADE_TYPE = FruitPunchGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, FruitPunchGatoradeFluid> FRUIT_PUNCH_GATORADE_SOURCE = FruitPunchGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> FRUIT_PUNCH_GATORADE_FLOWING = FruitPunchGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> LEMON_LIME_GATORADE_TYPE = LemonLimeGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, LemonLimeGatoradeFluid> LEMON_LIME_GATORADE_SOURCE = LemonLimeGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> LEMON_LIME_GATORADE_FLOWING = LemonLimeGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> ORANGE_GATORADE_TYPE = OrangeGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, OrangeGatoradeFluid> ORANGE_GATORADE_SOURCE = OrangeGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> ORANGE_GATORADE_FLOWING = OrangeGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> COOL_BLUE_GATORADE_TYPE = CoolBlueGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, CoolBlueGatoradeFluid> COOL_BLUE_GATORADE_SOURCE = CoolBlueGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> COOL_BLUE_GATORADE_FLOWING = CoolBlueGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> LIME_CUCUMBER_GATORADE_TYPE = LimeCucumberGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, LimeCucumberGatoradeFluid> LIME_CUCUMBER_GATORADE_SOURCE = LimeCucumberGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> LIME_CUCUMBER_GATORADE_FLOWING = LimeCucumberGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> LIGHTNING_BLAST_GATORADE_TYPE = LightningBlastGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, LightningBlastGatoradeFluid> LIGHTNING_BLAST_GATORADE_SOURCE = LightningBlastGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> LIGHTNING_BLAST_GATORADE_FLOWING = LightningBlastGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> MIDNIGHT_ICE_GATORADE_TYPE = MidnightIceGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, MidnightIceGatoradeFluid> MIDNIGHT_ICE_GATORADE_SOURCE = MidnightIceGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> MIDNIGHT_ICE_GATORADE_FLOWING = MidnightIceGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> GLACIER_FREEZE_GATORADE_TYPE = GlacierFreezeGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, GlacierFreezeGatoradeFluid> GLACIER_FREEZE_GATORADE_SOURCE = GlacierFreezeGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> GLACIER_FREEZE_GATORADE_FLOWING = GlacierFreezeGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> GLACIER_CHERRY_GATORADE_TYPE = GlacierCherryGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, GlacierCherryGatoradeFluid> GLACIER_CHERRY_GATORADE_SOURCE = GlacierCherryGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> GLACIER_CHERRY_GATORADE_FLOWING = GlacierCherryGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> ARCTIC_BLITZ_GATORADE_TYPE = ArcticBlitzGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, ArcticBlitzGatoradeFluid> ARCTIC_BLITZ_GATORADE_SOURCE = ArcticBlitzGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> ARCTIC_BLITZ_GATORADE_FLOWING = ArcticBlitzGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> GRAPE_GATORADE_TYPE = GrapeGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, GrapeGatoradeFluid> GRAPE_GATORADE_SOURCE = GrapeGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> GRAPE_GATORADE_FLOWING = GrapeGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> STRAWBERRY_GATORADE_TYPE = StrawberryGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, StrawberryGatoradeFluid> STRAWBERRY_GATORADE_SOURCE = StrawberryGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> STRAWBERRY_GATORADE_FLOWING = StrawberryGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> BLUE_CHERRY_GATORADE_TYPE = BlueCherryGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, BlueCherryGatoradeFluid> BLUE_CHERRY_GATORADE_SOURCE = BlueCherryGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> BLUE_CHERRY_GATORADE_FLOWING = BlueCherryGatoradeFluid.FLOWING;

    public static final DeferredHolder<FluidType, FluidType> GREEN_APPLE_GATORADE_TYPE = GreenAppleGatoradeFluid.TYPE;
    public static final DeferredHolder<Fluid, GreenAppleGatoradeFluid> GREEN_APPLE_GATORADE_SOURCE = GreenAppleGatoradeFluid.SOURCE;
    public static final DeferredHolder<Fluid, GatoradeFluid.FlowingGatoradeFluid> GREEN_APPLE_GATORADE_FLOWING = GreenAppleGatoradeFluid.FLOWING;

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}
