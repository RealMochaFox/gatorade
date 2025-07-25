package com.mochafox.gatorade.advancement.triggers;

import com.mochafox.gatorade.fluid.ModFluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.Set;

/**
 * Advancement trigger for drinking all types of gatorade fluids.
 * Tracks how many different gatorade types have been consumed.
 */
public class DrinkAllGatoradeTrigger extends SimpleCriterionTrigger<DrinkAllGatoradeTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Set<String> uniqueFluids) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(uniqueFluids));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player)
            implements SimpleCriterionTrigger.SimpleInstance {
        
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
            ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(Set<String> uniqueFluids) {
            return uniqueFluids.size() >= ModFluids.FLUIDS.getEntries().size();
        }
    }
}
