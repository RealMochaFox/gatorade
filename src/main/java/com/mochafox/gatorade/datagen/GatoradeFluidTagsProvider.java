package com.mochafox.gatorade.datagen;
import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.ModFluids;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

public class GatoradeFluidTagsProvider extends FluidTagsProvider {

    public GatoradeFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Gatorade.MODID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        TagAppender<Fluid, Fluid> waterTag = this.tag(FluidTags.WATER);
        
        // Add all Gatorade fluids to the water tag
        ModFluids.FLUIDS.getEntries().stream()
                .forEach(holder -> {
                    waterTag.add(holder.get());
                });
    }
}
