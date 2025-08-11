package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import javax.annotation.Nonnull;

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
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "gatorade_fluidhandler_item_tint_source"),
            GatoradeFluidHandlerItemTintSource.MAP_CODEC
        );
        event.register(
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "gatorade_bucket_item_tint_source"),
            GatoradeBucketItemTintSource.MAP_CODEC
        );
    }

    public static void registerConditionalItemProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(
            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "fluidhandler_item_has_fluid"),
            FluidHandlerItemHasFluidProperty.MAP_CODEC
        );
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // Register all Gatorade fluid client extensions
        ModFluids.FLUIDS.getEntries().forEach(entry -> {
            GatoradeFluid fluid = (GatoradeFluid) entry.get();
            if (fluid instanceof GatoradeFluid.SourceGatoradeFluid) {
                GatoradeFluid.SourceGatoradeFluid sourceFluid = (GatoradeFluid.SourceGatoradeFluid) fluid;
                registerGatoradeFluidExtension(event, sourceFluid.getFluidType(), sourceFluid.getTintColor());
            }

            ItemBlockRenderTypes.setRenderLayer(
                fluid,
                ChunkSectionLayer.TRANSLUCENT
            );
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
            private static final ResourceLocation RENDER_OVERLAY_TEXTURE = ResourceLocation.parse("minecraft:textures/misc/underwater.png");

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
            public ResourceLocation getRenderOverlayTexture(@Nonnull Minecraft mc) {
                return RENDER_OVERLAY_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }
        }, fluidType);
    }
}