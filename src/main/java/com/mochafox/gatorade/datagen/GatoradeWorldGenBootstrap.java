package com.mochafox.gatorade.datagen;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.ModFluids;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.LakeFeature.Configuration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collection;
import java.util.List;

public class GatoradeWorldGenBootstrap {
    private static final Collection<DeferredHolder<Fluid, ? extends Fluid>> fluidEntries = ModFluids.FLUIDS.getEntries();

    @SuppressWarnings("deprecation")
    public static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Register configured features for each gatorade fluid
        fluidEntries.stream()
                .map(h -> h.getId())
                .filter(id -> id.getNamespace().equals(Gatorade.MODID))
                .filter(id -> !id.getPath().endsWith("_flowing"))
                .forEach(id -> {
                    String baseName = id.getPath();
                    ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake")
                    );
                    
                    // Get the fluid block state
                    BlockState fluidState = fluidEntries.stream()
                            .filter(entry -> entry.getId().getPath().equals(baseName))
                            .findFirst()
                            .map(entry -> entry.get().defaultFluidState().createLegacyBlock())
                            .orElse(Blocks.WATER.defaultBlockState());
                    
                    // Create the lake feature configuration (using deprecated API for now)
                    Configuration config = new LakeFeature.Configuration(
                            SimpleStateProvider.simple(fluidState),
                            SimpleStateProvider.simple(Blocks.PACKED_ICE.defaultBlockState())
                    );
                    
                    context.register(key, new ConfiguredFeature<>(Feature.LAKE, config));
                });
    }
    
    public static void placedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Register placed features for each gatorade fluid
        fluidEntries.stream()
                .map(h -> h.getId())
                .filter(id -> id.getNamespace().equals(Gatorade.MODID))
                .filter(id -> !id.getPath().endsWith("_flowing"))
                .forEach(id -> {
                    String baseName = id.getPath();
                    Gatorade.LOGGER.info("Registering placed feature for: " + baseName);

                    ResourceKey<PlacedFeature> key = ResourceKey.create(
                            Registries.PLACED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake")
                    );
                    
                    ResourceKey<ConfiguredFeature<?, ?>> configuredKey = ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake")
                    );
                
                    int rarity = fluidEntries.size() * 200;
                    List<PlacementModifier> placement = List.of(
                            // Rarity
                            RarityFilter.onAverageOnceEvery(rarity),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(
                                    Heightmap.Types.WORLD_SURFACE_WG
                            ),
                            BiomeFilter.biome()
                    );
                    
                    context.register(key, new PlacedFeature(
                            configuredFeatures.getOrThrow(configuredKey), 
                            placement
                    ));
                });
    }
    
    public static void biomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        
        // Create a list of all gatorade lake placed features
        var gatoradeLakeFeatures = fluidEntries.stream()
                .map(h -> h.getId())
                .filter(id -> id.getNamespace().equals(Gatorade.MODID))
                .filter(id -> !id.getPath().endsWith("_flowing"))
                .map(id -> {
                    String baseName = id.getPath();
                    ResourceKey<PlacedFeature> key = ResourceKey.create(
                            Registries.PLACED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake")
                    );
                    return placedFeatures.getOrThrow(key);
                })
                .toList();
        
        HolderSet<PlacedFeature> featureSet = HolderSet.direct(gatoradeLakeFeatures);
        
        // Register the biome modifier to add all gatorade lakes to overworld biomes
        ResourceKey<BiomeModifier> key = ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "add_gatorade_lakes_overworld")
        );
        context.register(key, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                featureSet,
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }
}
