package com.mochafox.gatorade.advancement.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

/**
 * Advancement trigger for drinking any gatorade fluid.
 * Tracks cumulative count of gatorade drinks.
 */
public class DrinkGatoradeTrigger extends SimpleCriterionTrigger<DrinkGatoradeTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Fluid fluid, int count) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(fluid, count));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<String> fluidName, int minCount)
            implements SimpleCriterionTrigger.SimpleInstance {
        
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.STRING.optionalFieldOf("fluid").forGetter(TriggerInstance::fluidName),
                Codec.INT.optionalFieldOf("min_count", 1).forGetter(TriggerInstance::minCount)
            ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(Fluid fluid, int count) {
            if (count < this.minCount) {
                return false;
            }
            
            if (this.fluidName.isPresent()) {
                String expectedFluidName = this.fluidName.get();
                String actualFluidName = fluid.toString();
                return actualFluidName.contains(expectedFluidName);
            }
            
            // No specific fluid requirement, any gatorade fluid will do
            return fluid.toString().contains("gatorade");
        }
    }
}
