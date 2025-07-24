package com.mochafox.gatorade.item;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import com.mochafox.gatorade.block.ModBlocks;
import com.mochafox.gatorade.block.entity.GatoradeBucketBlockEntity;
import com.mochafox.gatorade.item.capability.SqueezeBottleFluidHandler;

/**
 * Event handler for registering item capabilities.
 */
public class CapabilityHandler {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register fluid handler capability for squeeze bottle
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            (itemStack, context) -> new SqueezeBottleFluidHandler(itemStack),
            ModItems.SQUEEZE_BOTTLE.get()
        );

        // Register fluid handler capability for gatorade bucket block
        event.registerBlock(
            Capabilities.FluidHandler.BLOCK,
            (level, pos, state, blockEntity, context) -> {
                if (blockEntity instanceof GatoradeBucketBlockEntity bucketEntity) {
                    return bucketEntity.getFluidHandler();
                }
                return null;
            },
            ModBlocks.GATORADE_BUCKET_BLOCK.get()
        );
    }
}
