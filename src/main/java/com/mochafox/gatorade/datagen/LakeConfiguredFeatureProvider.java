package com.mochafox.gatorade.datagen;

import com.google.gson.JsonObject;
import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.ModFluids;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LakeConfiguredFeatureProvider implements DataProvider {
    private final PathProvider pathProvider;

    public LakeConfiguredFeatureProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/configured_feature");
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
        root.addProperty("type", "minecraft:lake");

        JsonObject config = new JsonObject();
        root.add("config", config);

        // fluid provider
        JsonObject fluid = new JsonObject();
        config.add("fluid", fluid);
        fluid.addProperty("type", "minecraft:simple_state_provider");
        JsonObject fluidState = new JsonObject();
        fluid.add("state", fluidState);
        fluidState.addProperty("Name", ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_block").toString());
        JsonObject props = new JsonObject();
        props.addProperty("level", "0");
        fluidState.add("Properties", props);

        // barrier provider
        JsonObject barrier = new JsonObject();
        config.add("barrier", barrier);
        barrier.addProperty("type", "minecraft:simple_state_provider");
        JsonObject barrierState = new JsonObject();
        barrier.add("state", barrierState);
        barrierState.addProperty("Name", ResourceLocation.fromNamespaceAndPath("minecraft", "packed_ice").toString());

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, baseName + "_gatorade_lake");
        Path path = pathProvider.json(id);
        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Gatorade Lake Configured Features";
    }
}
