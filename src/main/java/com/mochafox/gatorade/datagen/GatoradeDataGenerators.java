package com.mochafox.gatorade.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import com.mochafox.gatorade.Gatorade;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GatoradeDataGenerators {
    public static void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Create the registry set builder for worldgen data
        var registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, GatoradeWorldGenBootstrap::configuredFeatures)
                .add(Registries.PLACED_FEATURE, GatoradeWorldGenBootstrap::placedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GatoradeWorldGenBootstrap::biomeModifiers);

        // Create the datapack builtin entries provider
        DataProvider worldgenProvider = new DatapackBuiltinEntriesProvider(
                packOutput, 
                lookupProvider, 
                registrySetBuilder, 
                Set.of(Gatorade.MODID)
        );
        generator.addProvider(true, worldgenProvider);

        // Add fluid tags provider
        generator.addProvider(true, new GatoradeFluidTagsProvider(packOutput, lookupProvider));
    }
}
