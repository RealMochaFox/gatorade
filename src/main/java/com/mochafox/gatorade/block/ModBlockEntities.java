package com.mochafox.gatorade.block;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.entity.GatoradeCoolerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Gatorade.MODID);

    public static final Supplier<BlockEntityType<GatoradeCoolerBlockEntity>> GATORADE_COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gatorade_cooler_block_entity", 
                    () -> new BlockEntityType<>(GatoradeCoolerBlockEntity::new, ModBlocks.GATORADE_COOLER_BLOCK.get()));
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
