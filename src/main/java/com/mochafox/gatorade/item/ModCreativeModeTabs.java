package com.mochafox.gatorade.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.ModBlocks;
import com.mochafox.gatorade.fluid.ModFluids;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;
import com.mochafox.gatorade.fluid.custom.OrangeGatoradeFluid;

/**
 * Registry class for mod creative mode tabs.
 */
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gatorade.MODID);

    // Creates a creative tab with the id "gatorade:gatorade_tab" for gatorade items
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GATORADE_TAB = CREATIVE_MODE_TABS.register("gatorade_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gatorade"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> createFilledSqueezeBottle(OrangeGatoradeFluid.SOURCE.get()))
            .displayItems((parameters, output) -> {
                
                // Empty squeeze bottle
                output.accept(ModItems.SQUEEZE_BOTTLE.get());
                
                // Infinite electrolyte drink (creative only)
                output.accept(ModItems.INFINITE_ELECTROLYTE_DRINK.get());

                // Gatorade cooler block
                output.accept(ModBlocks.GATORADE_COOLER_BLOCK.get());

                // Pre-filled squeeze bottle
                output.accept(createFilledSqueezeBottle(OrangeGatoradeFluid.SOURCE.get()));

                // Fluid buckets for spawning
                ModFluids.FLUIDS.getEntries().forEach(entry -> {
                    if (entry.get() instanceof GatoradeFluid.SourceGatoradeFluid) {
                        GatoradeFluid.SourceGatoradeFluid fluid = (GatoradeFluid.SourceGatoradeFluid) entry.get();
                        output.accept(fluid.getBucket());
                    }
                });
            }).build());

    private static ItemStack createFilledSqueezeBottle(net.minecraft.world.level.material.Fluid fluid) {
        ItemStack filledBottle = new ItemStack(ModItems.SQUEEZE_BOTTLE.get());
        var fluidHandler = filledBottle.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler != null) {
            FluidStack gatoradeFluid = new FluidStack(fluid, 1000);
            fluidHandler.fill(gatoradeFluid, IFluidHandler.FluidAction.EXECUTE);
            filledBottle = fluidHandler.getContainer();
        }
        return filledBottle;
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
