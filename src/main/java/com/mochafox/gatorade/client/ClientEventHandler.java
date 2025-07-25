package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.client.property.HasFluidProperty;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
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

    public static void registerConditionalItemProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "has_fluid"),
            HasFluidProperty.MAP_CODEC
        );
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // Register all Gatorade fluid client extensions
        ModFluids.FLUIDS.getEntries().forEach(entry -> {
            if (entry.get() instanceof GatoradeFluid.SourceGatoradeFluid) {
                GatoradeFluid.SourceGatoradeFluid fluid = (GatoradeFluid.SourceGatoradeFluid) entry.get();
                registerGatoradeFluidExtension(event, fluid.getFluidType(), fluid.getTintColor());
            }
        });
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
