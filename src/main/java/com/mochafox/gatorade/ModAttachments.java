package com.mochafox.gatorade;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.IEventBus;

import com.mochafox.gatorade.advancement.GatoradeAdvancementData;
import com.mochafox.gatorade.electrolytes.ElectrolytesData;

import java.util.function.Supplier;

/**
 * Registry for all mod attachment types
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Gatorade.MODID);
    
    // Attachment type for storing electrolytes data
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ElectrolytesData>> ELECTROLYTES_DATA = 
        ATTACHMENT_TYPES.register("electrolytes", () -> AttachmentType.builder(() -> new ElectrolytesData())
            .serialize(ElectrolytesData.CODEC.fieldOf("electrolytes"))
            .sync(ElectrolytesData.STREAM_CODEC)
            .build()
    );
    
    // Attachment type for storing Gatorade advancement data
    public static final Supplier<AttachmentType<GatoradeAdvancementData>> GATORADE_ADVANCEMENT_DATA =
            ATTACHMENT_TYPES.register("gatorade_advancement_data", () ->
                    AttachmentType.builder(() -> new GatoradeAdvancementData())
                    .serialize(GatoradeAdvancementData.CODEC.fieldOf("advancement_data"))
                    .copyOnDeath()
                    .build()
            );
    
    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
