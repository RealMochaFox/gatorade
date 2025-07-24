package com.mochafox.gatorade.electrolytes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.IEventBus;

import com.mochafox.gatorade.Gatorade;

/**
 * Registry for electrolytes-related attachment types.
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Gatorade.MODID);
    
    // Codec for ElectrolytesData serialization
    public static final Codec<ElectrolytesData> ELECTROLYTES_CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.INT.fieldOf("electrolytes").forGetter(ElectrolytesData::getElectrolytes),
            Codec.LONG.fieldOf("lastDecayTime").forGetter(ElectrolytesData::getLastDecayTime)
        ).apply(instance, (electrolytes, lastDecayTime) -> {
            ElectrolytesData data = new ElectrolytesData();
            data.setElectrolytes(electrolytes);
            data.setLastDecayTime(lastDecayTime);
            return data;
        })
    );
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ElectrolytesData>> ELECTROLYTES_DATA = 
        ATTACHMENT_TYPES.register("electrolytes", () -> AttachmentType.builder(() -> new ElectrolytesData())
            .serialize(ELECTROLYTES_CODEC.fieldOf("electrolytes"))
            .sync(ElectrolytesData.STREAM_CODEC)
            .copyOnDeath()
            .build()
    );
    
    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
