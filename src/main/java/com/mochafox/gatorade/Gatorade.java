package com.mochafox.gatorade;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import com.mochafox.gatorade.block.ModBlocks;
import com.mochafox.gatorade.block.ModBlockEntities;
import com.mochafox.gatorade.electrolytes.ModAttachments;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.item.CapabilityHandler;
import com.mochafox.gatorade.item.ModCreativeModeTabs;
import com.mochafox.gatorade.item.ModDataComponents;
import com.mochafox.gatorade.item.ModItems;

@Mod(Gatorade.MODID)
public class Gatorade {
    public static final String MODID = "gatorade";
    public static final int DEFAULT_FLUID_COLOR = 0x3C44AA;
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gatorade(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register all mod registries
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModFluids.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);

        // Register capabilities
        modEventBus.addListener(CapabilityHandler::registerCapabilities);

        // Register config
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("ARE YOU JUICED UP? GATORADE MOD IS SLAMMING INTO YOUR UNIVERSE!");
    }
}
