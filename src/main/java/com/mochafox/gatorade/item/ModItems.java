package com.mochafox.gatorade.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.mochafox.gatorade.Gatorade;

import com.mochafox.gatorade.item.custom.InfiniteElectrolyteDrinkItem;
import com.mochafox.gatorade.item.custom.SqueezeBottleItem;

/**
 * Registry class for all mod items.
 */
public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gatorade.MODID);

    // Squeeze Bottle item
    public static final DeferredItem<Item> SQUEEZE_BOTTLE = ITEMS.registerItem("squeeze_bottle", 
        SqueezeBottleItem::create);

    // Infinite electrolyte drink item
    public static final DeferredItem<Item> INFINITE_ELECTROLYTE_DRINK = ITEMS.registerItem("infinite_electrolyte_drink",
        InfiniteElectrolyteDrinkItem::create);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
