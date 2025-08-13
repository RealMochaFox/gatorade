package com.mochafox.gatorade.electrolytes;

import com.mochafox.gatorade.commands.ElectrolytesCommand;
import com.mochafox.gatorade.Config;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
public class ElectrolytesEvents {

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ElectrolytesCommand.register(event.getDispatcher());
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // Only run on server side
        if (player.level().isClientSide) {
            return;
        }
        
        // Check if electrolytes system is enabled
        if (!Config.ENABLE_ELECTROLYTES.get()) {
            return;
        }

        // In creative mode, electrolytes are not active
        if (player.isCreative()) {
            return;
        }
        
        // Tick electrolytes (decay over time and with activity)
        ElectrolytesUtil.tickElectrolytes(player);
        
        // Apply effects based on electrolyte levels
        applyElectrolyteEffects(player);
    }
    
    /**
     * Applies effects based on the player's electrolyte levels.
     */
    private static void applyElectrolyteEffects(Player player) {
        ElectrolytesData data = ElectrolytesUtil.getElectrolytes(player);
        int effectStage = data.getEffectStage();
        
        if (effectStage > 0) {
            // Positive stages
            if (effectStage == 3) {
                player.addEffect(new MobEffectInstance(MobEffects.HASTE, 0, 1, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 0, 1, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 0, 1, false, false, false));
            } else if (effectStage == 2) {
                player.addEffect(new MobEffectInstance(MobEffects.HASTE, 0, 0, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 0, 0, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 0, 0, false, false, false));
            } else if (effectStage == 1) {
                player.addEffect(new MobEffectInstance(MobEffects.HASTE, 0, 0, false, false, false));
            }

        } else if (effectStage < 0) {
            // Negative stages
            int effectLevel = (effectStage * -1) - 1;
            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 0, effectLevel, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 0, effectLevel, false, false, false));

            if (effectStage <= -3) {
                int hungerLevel = (effectStage == -3) ? 0 : 1;

                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 0, hungerLevel, false, false, false));
            }
        }
    }
}
