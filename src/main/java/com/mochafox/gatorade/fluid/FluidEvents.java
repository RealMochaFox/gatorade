package com.mochafox.gatorade.fluid;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mochafox.gatorade.Config;
import com.mochafox.gatorade.electrolytes.ElectrolytesUtil;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidEvents {
    private static long lastFluidTick;
    private static Set<FluidType> gatoradeFluidTypes;
    private static Set<UUID> playersInFluid = new HashSet<>(); // unsure if this is the right way to do this

    private static Set<FluidType> getGatoradeFluidTypes() {
        if (gatoradeFluidTypes == null) {
            gatoradeFluidTypes = ModFluids.FLUID_TYPES.getEntries().stream()
                .map(holder -> holder.get())
                .collect(Collectors.toUnmodifiableSet());
        }
        return gatoradeFluidTypes;
    }


    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            long currentTime = player.level().getGameTime();
        
            // Skip if no time has passed
            if (currentTime <= lastFluidTick) {
                return;
            }
            
            boolean isInGatoradeFluid = player.isInFluidType((fluidType, height) -> getGatoradeFluidTypes().contains(fluidType) && height > 0.0);
            UUID playerId = player.getUUID();
            boolean wasInFluid = playersInFluid.contains(playerId);
            
            if (isInGatoradeFluid) {
                // If player just entered the fluid, play splash sound
                if (!wasInFluid) {
                    playersInFluid.add(playerId);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
                        SoundEvents.PLAYER_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                applyElectrolyteEffects(player, currentTime);
                applyPhysicalGatoradeEffects(player);

                lastFluidTick = currentTime;
            } else if (wasInFluid) {
                // Player left the fluid, remove from tracking
                playersInFluid.remove(playerId);
            }

        }
    }

    private static void applyElectrolyteEffects(Player player, long currentTime) {
        boolean gatoradeBathingRegenerationElectrolyteEffects = Config.GATORADE_BATHING_REGENERATION_ELECTROLYTE_EFFECTS.get();
        if (!gatoradeBathingRegenerationElectrolyteEffects) return;

        int gatoradeBathingRegenerationRate = Config.GATORADE_BATHING_REGENERATION_RATE.get();
        int gatoradeBathingRegenerationAmount = Config.GATORADE_BATHING_REGENERATION_AMOUNT.get();

        if (currentTime % gatoradeBathingRegenerationRate == 0) {
            ElectrolytesUtil.addElectrolytes(player, gatoradeBathingRegenerationAmount);
            player.level().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.5, player.getZ(), 0, 0, 0);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_CELEBRATE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static void applyPhysicalGatoradeEffects(Player player) {
        boolean gatoradeBathingRegenerationPhysicalEffects = Config.GATORADE_BATHING_REGENERATION_PHYSICAL_EFFECTS.get();
        if (!gatoradeBathingRegenerationPhysicalEffects) return;

        if (!player.hasEffect(MobEffects.REGENERATION)) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        }

        if (!player.hasEffect(MobEffects.SPEED)) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 200, 0));
        }
    }
}
