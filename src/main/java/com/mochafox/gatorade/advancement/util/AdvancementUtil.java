package com.mochafox.gatorade.advancement.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import com.mochafox.gatorade.advancement.ModAdvancementTriggers;
import com.mochafox.gatorade.advancement.GatoradeAdvancementData;
import com.mochafox.gatorade.ModAttachments;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;

/**
 * Utility class for tracking and triggering gatorade-related advancements.
 */
public class AdvancementUtil {
    public static void recordGatoradeConsumption(ServerPlayer player, Fluid fluid) {
        if (!(fluid instanceof GatoradeFluid)) {
            return;
        }
        
        GatoradeAdvancementData data = player.getData(ModAttachments.GATORADE_ADVANCEMENT_DATA.get());
        
        String fluidName = fluid.toString();
        
        data.incrementGatoradeDrinks();
        data.addUniqueGatoradeFluid(fluidName);
        
        ModAdvancementTriggers.DRINK_GATORADE_TRIGGER.get().trigger(player, fluid, data.getTotalGatoradeDrinks());
        ModAdvancementTriggers.DRINK_UNIQUE_GATORADE_TRIGGER.get().trigger(player, data.getUniqueGatoradeFluids());
        
        player.setData(ModAttachments.GATORADE_ADVANCEMENT_DATA.get(), data);
    }
    
    /**
     * Gets the total number of gatorade drinks consumed by a player.
     */
    public static int getTotalGatoradeDrinks(ServerPlayer player) {
        return player.getData(ModAttachments.GATORADE_ADVANCEMENT_DATA.get()).getTotalGatoradeDrinks();
    }
    
    /**
     * Gets the number of unique gatorade types consumed by a player.
     */
    public static int getUniqueGatoradeCount(ServerPlayer player) {
        return player.getData(ModAttachments.GATORADE_ADVANCEMENT_DATA.get()).getUniqueGatoradeFluids().size();
    }
}
