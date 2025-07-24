package com.mochafox.gatorade.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.fluid.custom.ArcticBlitzGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.BlueCherryGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.CoolBlueGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.FruitPunchGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GlacierCherryGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GlacierFreezeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GrapeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.GreenAppleGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LemonLimeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LightningBlastGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.LimeCucumberGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.MidnightIceGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.OrangeGatoradeFluid;
import com.mochafox.gatorade.fluid.custom.StrawberryGatoradeFluid;
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

    // Gatorade bucket items
    public static final DeferredItem<BucketItem> FRUIT_PUNCH_GATORADE_BUCKET = FruitPunchGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> LEMON_LIME_GATORADE_BUCKET = LemonLimeGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> ORANGE_GATORADE_BUCKET = OrangeGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> COOL_BLUE_GATORADE_BUCKET = CoolBlueGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> LIME_CUCUMBER_GATORADE_BUCKET = LimeCucumberGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> LIGHTNING_BLAST_GATORADE_BUCKET = LightningBlastGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> MIDNIGHT_ICE_GATORADE_BUCKET = MidnightIceGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> GLACIER_FREEZE_GATORADE_BUCKET = GlacierFreezeGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> GLACIER_CHERRY_GATORADE_BUCKET = GlacierCherryGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> ARCTIC_BLITZ_GATORADE_BUCKET = ArcticBlitzGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> GRAPE_GATORADE_BUCKET = GrapeGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> STRAWBERRY_GATORADE_BUCKET = StrawberryGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> BLUE_CHERRY_GATORADE_BUCKET = BlueCherryGatoradeFluid.BUCKET;
    public static final DeferredItem<BucketItem> GREEN_APPLE_GATORADE_BUCKET = GreenAppleGatoradeFluid.BUCKET;

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
