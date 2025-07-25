package com.mochafox.gatorade.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.mochafox.gatorade.Gatorade;

/**
 * Registry for mod data components.
 */
public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = 
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Gatorade.MODID);

    // Data component for storing fluid content in squeeze bottles
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> SQUEEZE_BOTTLE_FLUID_CONTENT = 
        DATA_COMPONENT_TYPES.register("squeeze_bottle_fluid_content", 
            () -> DataComponentType.<SimpleFluidContent>builder()
                .persistent(SimpleFluidContent.CODEC)
                .networkSynchronized(SimpleFluidContent.STREAM_CODEC)
                .build());

    // Data component for storing fluid content in gatorade bucket blocks
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> GATORADE_BUCKET_FLUID_CONTENT = 
        DATA_COMPONENT_TYPES.register("gatorade_bucket_fluid_content", 
            () -> DataComponentType.<SimpleFluidContent>builder()
                .persistent(SimpleFluidContent.CODEC)
                .networkSynchronized(SimpleFluidContent.STREAM_CODEC)
                .build());

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENT_TYPES.register(modEventBus);
    }
}
