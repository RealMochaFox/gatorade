package com.mochafox.gatorade.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public class GatoradeDataGenerators {
    public static void onGatherData(final GatherDataEvent event) {
        event.createProvider(LakeConfiguredFeatureProvider::new);
        event.createProvider(LakePlacedFeatureProvider::new);
        event.createProvider(LakeBiomeModifierProvider::new);
        event.createProvider(GatoradeFluidTagsProvider::new);
    }
}
