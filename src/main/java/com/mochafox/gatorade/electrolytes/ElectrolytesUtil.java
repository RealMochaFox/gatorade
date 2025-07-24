package com.mochafox.gatorade.electrolytes;

import net.minecraft.world.entity.player.Player;

/**
 * Utility class for managing player electrolytes.
 */
public class ElectrolytesUtil {
    
    /**
     * Gets the electrolytes data for a player.
     */
    public static ElectrolytesData getElectrolytes(Player player) {
        return player.getData(ModAttachments.ELECTROLYTES_DATA);
    }
    
    /**
     * Sets the electrolytes data for a player.
     */
    public static void setElectrolytes(Player player, ElectrolytesData data) {
        player.setData(ModAttachments.ELECTROLYTES_DATA, data);
    }
    
    /**
     * Gets the current electrolyte level for a player.
     */
    public static int getElectrolyteLevel(Player player) {
        return getElectrolytes(player).getElectrolytes();
    }
    
    /**
     * Sets the electrolyte level for a player.
     */
    public static void setElectrolyteLevel(Player player, int amount) {
        ElectrolytesData data = getElectrolytes(player);
        data.setElectrolytes(amount);
        setElectrolytes(player, data);
    }
    
    /**
     * Adds electrolytes to a player.
     */
    public static void addElectrolytes(Player player, int amount) {
        ElectrolytesData data = getElectrolytes(player);
        data.addElectrolytes(amount);
        setElectrolytes(player, data);
    }
    
    /**
     * Removes electrolytes from a player.
     */
    public static void removeElectrolytes(Player player, int amount) {
        ElectrolytesData data = getElectrolytes(player);
        data.removeElectrolytes(amount);
        setElectrolytes(player, data);
    }
    
    /**
     * Gets the electrolyte percentage (0.0 to 1.0) for a player.
     */
    public static float getElectrolytePercentage(Player player) {
        return getElectrolytes(player).getElectrolytePercentage();
    }
    
    /**
     * Checks if a player's electrolytes are low.
     */
    public static boolean areElectrolytesLow(Player player) {
        return getElectrolytes(player).isLow();
    }
    
    /**
     * Checks if a player's electrolytes are critically low.
     */
    public static boolean areElectrolytesCritical(Player player) {
        return getElectrolytes(player).isCritical();
    }
    
    /**
     * Gets the current electrolyte effect stage for a player.
     * Returns 0-4 where 0 = no effects, 1-4 = progressive effect stages.
     */
    public static int getElectrolyteEffectStage(Player player) {
        return getElectrolytes(player).getEffectStage();
    }
    
    /**
     * Handles electrolyte decay for a player (called from tick event).
     */
    public static void tickElectrolytes(Player player) {
        ElectrolytesData data = getElectrolytes(player);
        data.tick(player);
        setElectrolytes(player, data);
    }
}
