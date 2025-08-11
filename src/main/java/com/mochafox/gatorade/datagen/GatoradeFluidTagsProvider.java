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
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class GatoradeFluidTagsProvider implements DataProvider {
    private final PathProvider pathProvider;

    public GatoradeFluidTagsProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/fluid");
    }

    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        return generateWaterTag(cache);
    }

    private CompletableFuture<?> generateWaterTag(CachedOutput cache) {
        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        
        JsonArray values = new JsonArray();
        
        // Add all Gatorade source and flowing fluids to the water tag
        ModFluids.FLUIDS.getEntries().stream()
                .filter(holder -> holder.getId().getNamespace().equals(Gatorade.MODID))
                .forEach(holder -> values.add(holder.getId().toString()));
        
        json.add("values", values);
        
        Path path = this.pathProvider.json(ResourceLocation.withDefaultNamespace("water"));
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Gatorade Fluid Tags";
    }
}
