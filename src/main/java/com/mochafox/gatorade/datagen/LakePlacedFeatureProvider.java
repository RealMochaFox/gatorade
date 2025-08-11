package com.mochafox.gatorade.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.ModFluids;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LakePlacedFeatureProvider implements DataProvider {
    private final PathProvider pathProvider;

    public LakePlacedFeatureProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/placed_feature");
    }

    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        ModFluids.FLUIDS.getEntries().stream()
                .map(h -> h.getId())
                .filter(id -> id.getNamespace().equals(Gatorade.MODID))
                .filter(id -> !id.getPath().endsWith("_flowing"))
                .forEach(id -> futures.add(saveForBaseName(cache, id.getPath())));
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> saveForBaseName(CachedOutput cache, String baseName) {
        JsonObject root = new JsonObject();
        root.addProperty("feature", Gatorade.MODID + ":" + baseName + "_gatorade_lake");

        JsonArray placement = new JsonArray();
        root.add("placement", placement);

        JsonObject rarity = new JsonObject();
        rarity.addProperty("type", "minecraft:rarity_filter");
        rarity.addProperty("chance", 2000);
        placement.add(rarity);

        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");
        placement.add(inSquare);

        JsonObject heightmap = new JsonObject();
        heightmap.addProperty("type", "minecraft:heightmap");
        heightmap.addProperty("heightmap", "MOTION_BLOCKING_NO_LEAVES");
        placement.add(heightmap);

        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");
        placement.add(biome);

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake");
        var path = pathProvider.json(id);
        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Gatorade Lake Placed Features";
    }
}
