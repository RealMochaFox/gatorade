package com.mochafox.gatorade.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.custom.GatoradeBucketBlock;
import com.mochafox.gatorade.item.ModItems;

import java.util.function.Function;

/**
 * Registry class for all mod blocks.
 */
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gatorade.MODID);

    public static final DeferredBlock<Block> GATORADE_BUCKET_BLOCK = registerBlock("gatorade_bucket_block",
            properties -> new GatoradeBucketBlock(properties.strength(2f).noLootTable().sound(SoundType.STONE)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
            
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}