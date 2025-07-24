package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.custom.ArcticBlitzGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.BlueCherryGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.CoolBlueGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.FruitPunchGatoradeFluid;
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

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Client-side event handlers and registrations.
 */
public class ClientEventHandler {
    
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
            VanillaGuiLayers.FOOD_LEVEL,
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "electrolytes"),
            new ElectrolytesHudOverlay()
        );
    }

    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "gatorade_fluid_item_tint_source"),
            GatoradeFluidItemTintSource.MAP_CODEC
        );
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // Register all Gatorade fluid client extensions
        registerGatoradeFluidExtension(event, FruitPunchGatoradeFluid.TYPE.get(), 0xFFFF0000);
        registerGatoradeFluidExtension(event, LemonLimeGatoradeFluid.TYPE.get(), 0xFF00FF00);
        registerGatoradeFluidExtension(event, OrangeGatoradeFluid.TYPE.get(), 0xFFFF8000);
        registerGatoradeFluidExtension(event, CoolBlueGatoradeFluid.TYPE.get(), 0xFF0080FF);
        registerGatoradeFluidExtension(event, LimeCucumberGatoradeFluid.TYPE.get(), 0xFF80FF80);
        registerGatoradeFluidExtension(event, LightningBlastGatoradeFluid.TYPE.get(), 0xFFFFFF00);
        registerGatoradeFluidExtension(event, MidnightIceGatoradeFluid.TYPE.get(), 0xFF200040);
        registerGatoradeFluidExtension(event, GlacierFreezeGatoradeFluid.TYPE.get(), 0xFF40C0FF);
        registerGatoradeFluidExtension(event, GlacierCherryGatoradeFluid.TYPE.get(), 0xFFFF4080);
        registerGatoradeFluidExtension(event, ArcticBlitzGatoradeFluid.TYPE.get(), 0xFFC0FFFF);
        registerGatoradeFluidExtension(event, GrapeGatoradeFluid.TYPE.get(), 0xFF8000FF);
        registerGatoradeFluidExtension(event, StrawberryGatoradeFluid.TYPE.get(), 0xFFFF6080);
        registerGatoradeFluidExtension(event, BlueCherryGatoradeFluid.TYPE.get(), 0xFF4060FF);
        registerGatoradeFluidExtension(event, GreenAppleGatoradeFluid.TYPE.get(), 0xFF40FF40);
    }

    /**
     * Helper method to register client-side fluid type extensions for Gatorade fluids.
     * This reduces code duplication by creating a standardized registration.
     * 
     * @param event The client extensions registration event
     * @param fluidType The fluid type to register extensions for
     * @param tintColor The ARGB color to tint the fluid with
     */
    private static void registerGatoradeFluidExtension(RegisterClientExtensionsEvent event, 
                                                      net.neoforged.neoforge.fluids.FluidType fluidType, 
                                                      int tintColor) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final ResourceLocation STILL_TEXTURE = ResourceLocation.parse("minecraft:block/water_still");
            private static final ResourceLocation FLOWING_TEXTURE = ResourceLocation.parse("minecraft:block/water_flow");
            private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.parse("minecraft:block/water_overlay");

            @Override
            public ResourceLocation getStillTexture() {
                return STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return OVERLAY_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }
        }, fluidType);
    }
}
