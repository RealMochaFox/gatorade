package com.mochafox.gatorade.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data container for tracking player advancement progress related to gatorade mod.
 */
public class GatoradeAdvancementData {
    public static final Codec<GatoradeAdvancementData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("totalGatoradeDrinks").forGetter(GatoradeAdvancementData::getTotalGatoradeDrinks),
            Codec.list(Codec.STRING).fieldOf("uniqueGatoradeFluids").forGetter(data -> List.copyOf(data.getUniqueGatoradeFluids()))
        ).apply(instance, (drinks, fluids) -> {
            GatoradeAdvancementData data = new GatoradeAdvancementData();
            data.totalGatoradeDrinks = drinks;
            data.uniqueGatoradeFluids = new HashSet<>(fluids);
            return data;
        })
    );
    private int totalGatoradeDrinks = 0;
    private Set<String> uniqueGatoradeFluids = new HashSet<>();

    public int getTotalGatoradeDrinks() {
        return totalGatoradeDrinks;
    }

    public void incrementGatoradeDrinks() {
        this.totalGatoradeDrinks++;
    }

    public Set<String> getUniqueGatoradeFluids() {
        return new HashSet<>(uniqueGatoradeFluids);
    }

    public void addUniqueGatoradeFluid(String fluidName) {
        this.uniqueGatoradeFluids.add(fluidName);
    }

    public boolean hasConsumedFluid(String fluidName) {
        return this.uniqueGatoradeFluids.contains(fluidName);
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("totalGatoradeDrinks", this.totalGatoradeDrinks);
        
        ListTag fluidsList = new ListTag();
        for (String fluid : this.uniqueGatoradeFluids) {
            fluidsList.add(StringTag.valueOf(fluid));
        }
        tag.put("uniqueGatoradeFluids", fluidsList);
        
        return tag;
    }

    public void load(HolderLookup.Provider provider, CompoundTag tag) {
        this.totalGatoradeDrinks = tag.getInt("totalGatoradeDrinks").orElse(0);
        
        this.uniqueGatoradeFluids.clear();
        if (tag.contains("uniqueGatoradeFluids")) {
            ListTag fluidsList = tag.getList("uniqueGatoradeFluids").orElse(new ListTag());
            for (int i = 0; i < fluidsList.size(); i++) {
                String fluidName = fluidsList.getString(i).orElse("");
                if (!fluidName.isEmpty()) {
                    this.uniqueGatoradeFluids.add(fluidName);
                }
            }
        }
    }

    public void reset() {
        this.totalGatoradeDrinks = 0;
        this.uniqueGatoradeFluids.clear();
    }
}
