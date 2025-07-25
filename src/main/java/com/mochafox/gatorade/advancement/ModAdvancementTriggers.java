package com.mochafox.gatorade.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.advancement.triggers.DrinkGatoradeTrigger;
import com.mochafox.gatorade.advancement.triggers.DrinkUniqueGatoradeTrigger;

/**
 * Registry for mod advancement triggers.
 */
public class ModAdvancementTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Gatorade.MODID);

    public static final Supplier<DrinkGatoradeTrigger> DRINK_GATORADE_TRIGGER =
            TRIGGER_TYPES.register("drink_gatorade", DrinkGatoradeTrigger::new);

    public static final Supplier<DrinkUniqueGatoradeTrigger> DRINK_UNIQUE_GATORADE_TRIGGER =
            TRIGGER_TYPES.register("drink_unique_gatorade", DrinkUniqueGatoradeTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGER_TYPES.register(eventBus);
    }
}
