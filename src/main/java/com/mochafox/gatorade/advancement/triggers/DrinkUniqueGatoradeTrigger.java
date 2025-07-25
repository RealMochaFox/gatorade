package com.mochafox.gatorade.advancement.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.Set;

/**
 * Advancement trigger for drinking unique types of gatorade fluids.
 * Tracks how many different gatorade types have been consumed.
 */
public class DrinkUniqueGatoradeTrigger extends SimpleCriterionTrigger<DrinkUniqueGatoradeTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Set<String> uniqueFluids) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(uniqueFluids));
    }

    // TODO: For drinks all gatorade types advancement
    // - Dynamically determine unique fluid count based on registered gatorade types
    
    public record TriggerInstance(Optional<ContextAwarePredicate> player, int minUniqueCount)
            implements SimpleCriterionTrigger.SimpleInstance {
        
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.optionalFieldOf("min_unique_count", 1).forGetter(TriggerInstance::minUniqueCount)
            ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(Set<String> uniqueFluids) {
            return uniqueFluids.size() >= this.minUniqueCount;
        }
    }
}
