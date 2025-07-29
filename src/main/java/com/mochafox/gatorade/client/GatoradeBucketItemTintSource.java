package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;
import com.mochafox.gatorade.item.custom.GatoradeBucketItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mojang.serialization.MapCodec;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public record GatoradeBucketItemTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<GatoradeBucketItemTintSource> MAP_CODEC = ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
        .xmap(color -> new GatoradeBucketItemTintSource(ARGB.opaque(color)), source -> source.defaultColor);

    public GatoradeBucketItemTintSource() {
        this(ARGB.opaque(Gatorade.DEFAULT_FLUID_COLOR));
    }

    @Override
    public int calculate(@Nonnull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        Item item = stack.getItem();

        if (!(item instanceof GatoradeBucketItem)) {
            return defaultColor;
        }

        Fluid fluid = ((GatoradeBucketItem) item).content;
        if (fluid instanceof GatoradeFluid) {
            GatoradeFluid gatoradeFluid = (GatoradeFluid) fluid;
            int tintColor = gatoradeFluid.getTintColor();
            return ARGB.opaque(tintColor);
        }

        return defaultColor;
    }

    @Override
    public MapCodec<GatoradeBucketItemTintSource> type() {
        return MAP_CODEC;
    }
}