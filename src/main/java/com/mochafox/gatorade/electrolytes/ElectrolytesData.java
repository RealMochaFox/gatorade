package com.mochafox.gatorade.electrolytes;

import com.mochafox.gatorade.Config;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

/**
 * Data class for tracking a player's electrolyte levels.
 */
public class ElectrolytesData {
    public static final Codec<ElectrolytesData> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Codec.INT.fieldOf("electrolytes").forGetter(ElectrolytesData::getElectrolytes),
            Codec.LONG.fieldOf("lastDecayTime").forGetter(ElectrolytesData::getLastDecayTime)
        ).apply(instance, (electrolytes, lastDecayTime) -> {
            ElectrolytesData data = new ElectrolytesData();
            data.setElectrolytes(electrolytes);
            data.setLastDecayTime(lastDecayTime);
            return data;
        })
    );
    
    public static int getMaxElectrolytes() {
        return Config.MAX_ELECTROLYTES.get();
    }
    
    public static int getDefaultElectrolytes() {
        return Config.DEFAULT_ELECTROLYTES.get();
    }
    
    // StreamCodec for network synchronization
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectrolytesData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ElectrolytesData::getElectrolytes,
        ByteBufCodecs.LONG,
        ElectrolytesData::getLastDecayTime,
        (electrolytes, lastDecayTime) -> {
            ElectrolytesData data = new ElectrolytesData();
            data.setElectrolytes(electrolytes);
            data.setLastDecayTime(lastDecayTime);
            return data;
        }
    );
    
    private int electrolytes;
    private long lastDecayTime;
    
    public ElectrolytesData() {
        this.electrolytes = getDefaultElectrolytes();
        this.lastDecayTime = 0;
    }
    
    /**
     * Gets the current electrolyte level.
     */
    public int getElectrolytes() {
        return electrolytes;
    }
    
    /**
     * Sets the electrolyte level, clamped to valid range.
     */
    public void setElectrolytes(int amount) {
        this.electrolytes = Math.max(0, Math.min(getMaxElectrolytes(), amount));
    }
    
    /**
     * Adds electrolytes, clamped to maximum.
     */
    public void addElectrolytes(int amount) {
        setElectrolytes(this.electrolytes + amount);
    }
    
    /**
     * Removes electrolytes, clamped to minimum.
     */
    public void removeElectrolytes(int amount) {
        setElectrolytes(this.electrolytes - amount);
    }
    
    /**
     * Gets the electrolyte level as a percentage (0.0 to 1.0).
     */
    public float getElectrolytePercentage() {
        return (float) electrolytes / getMaxElectrolytes();
    }
    
    /**
     * Checks if electrolytes are low
     */
    public boolean isLow() {
        return electrolytes < (getMaxElectrolytes() * 0.29f);
    }
    
    /**
     * Checks if electrolytes are critically low
     */
    public boolean isCritical() {
        return electrolytes < (getMaxElectrolytes() * 0.1f);
    }
    
    /**
     * Gets the current electrolyte effect stage (0 = no effects, 1-4 = progressive stages).
     * Stage 3 (95-100%): Haste II + Speed II + Jump Boost II
     * Stage 2 (80-95%): Haste I + Speed I + Jump Boost I
     * Stage 1 (80-90%): Haste I
     * Stage 0 (30-70%): No effects
     * Stage -1 (20-30%): Slowness I + Mining Fatigue I
     * Stage -2 (10-20%): Slowness II + Mining Fatigue II  
     * Stage -3 (1-10%): Slowness III + Mining Fatigue III + Hunger I
     * Stage -4 (0-1%): Slowness III + Mining Fatigue III + Hunger II
     */
    public int getEffectStage() {
        int maxElectrolytes = getMaxElectrolytes();
        if (electrolytes >= (maxElectrolytes * 0.95f)) {
            return 3;
        } else if (electrolytes >= (maxElectrolytes * 0.8f)) {
            return 2;
        } else if (electrolytes >= (maxElectrolytes * 0.7f)) {
            return 1;
        } else if (electrolytes >= (maxElectrolytes * 0.3f)) {
            return 0;
        } else if (electrolytes >= (maxElectrolytes * 0.2f)) {
            return -1;
        } else if (electrolytes >= (maxElectrolytes * 0.1f)) {
            return -2;
        } else if (electrolytes >= (maxElectrolytes * 0.01f)) {
            return -3;
        } else {
            return -4;
        }
    }
    
    /**
     * Gets the last time electrolytes decayed (in world time).
     */
    public long getLastDecayTime() {
        return lastDecayTime;
    }
    
    /**
     * Sets the last decay time.
     */
    public void setLastDecayTime(long time) {
        this.lastDecayTime = time;
    }
    
    /**
     * Handles electrolyte decay over time and from activities.
     */
    public void tick(Player player) {
        long currentTime = player.level().getGameTime();
        
        // Skip if no time has passed
        if (currentTime <= lastDecayTime) {
            return;
        }
        
        // Natural decay over time (configurable rate)
        int decayRate = Config.ELECTROLYTES_DECAY_RATE.get();
        if (currentTime % decayRate == 0) {
            removeElectrolytes(1);
        }
        
        // Additional decay from movement (check if player is moving)
        if (player.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
            int moveDecayRate = Config.ELECTROLYTES_MOVE_DECAY_RATE.get();
            if (currentTime % moveDecayRate == 0) {
                removeElectrolytes(1);
            }
        }
        
        // Extra decay from sprinting
        if (player.isSprinting()) {
            int sprintDecayRate = Config.ELECTROLYTES_SPRINT_DECAY_RATE.get();
            if (currentTime % sprintDecayRate == 0) {
                removeElectrolytes(2);
            }
        }
        
        lastDecayTime = currentTime;
    }
    
    /**
     * Saves the electrolytes data.
     */
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("electrolytes", electrolytes);
        tag.putLong("lastDecayTime", lastDecayTime);
        return tag;
    }
    
    /**
     * Loads the electrolytes data.
     */
    public void load(CompoundTag tag) {
        this.electrolytes = tag.getInt("electrolytes").orElse(getDefaultElectrolytes());
        this.lastDecayTime = tag.getLong("lastDecayTime").orElse(0L);
    }
}
