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
import java.util.concurrent.CompletableFuture;

public class LakeBiomeModifierProvider implements DataProvider {
    private final PathProvider pathProvider;

    public LakeBiomeModifierProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "neoforge/biome_modifier");
    }

    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "neoforge:add_features");
        root.addProperty("biomes", "#minecraft:is_overworld");

        JsonArray features = new JsonArray();
        ModFluids.FLUIDS.getEntries().stream()
                .map(h -> h.getId())
                .filter(id -> id.getNamespace().equals(Gatorade.MODID))
                .filter(id -> !id.getPath().endsWith("_flowing"))
                .forEach(id -> features.add(Gatorade.MODID + ":" + id.getPath() + "_gatorade_lake"));
        root.add("features", features);
        root.addProperty("step", "lakes");

        var id = ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "add_gatorade_lakes_overworld");
        var path = pathProvider.json(id);
        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Gatorade Biome Modifiers (Lakes)";
    }
}
