package com.mochafox.gatorade.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * MC 1.21.x:
 * Redirect the call to FluidState#is(TagKey) that happens inside LakeFeature.place(...).
 * Returning false prevents the lake freeze logic path that queries neighbour biomes/chunks,
 * eliminating the "Requested chunk unavailable during world generation" crash.
 */
@SuppressWarnings("deprecation")
@Mixin(LakeFeature.class)
public class LakeFeatureFreezeCheckRedirectMixin {

    @Redirect(
        method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"
        ),
        require = 0
    )
    private boolean gatorade$redirectFluidIs(FluidState self, TagKey<?> tag) {
        return false;
    }
}
