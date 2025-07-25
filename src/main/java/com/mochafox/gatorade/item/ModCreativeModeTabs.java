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
                output.accept(OrangeGatoradeFluid.BUCKET.get());
                output.accept(FruitPunchGatoradeFluid.BUCKET.get());
                output.accept(LemonLimeGatoradeFluid.BUCKET.get());
                output.accept(CoolBlueGatoradeFluid.BUCKET.get());
                output.accept(LimeCucumberGatoradeFluid.BUCKET.get());
                output.accept(LightningBlastGatoradeFluid.BUCKET.get());
                output.accept(MidnightIceGatoradeFluid.BUCKET.get());
                output.accept(GlacierFreezeGatoradeFluid.BUCKET.get());
                output.accept(GlacierCherryGatoradeFluid.BUCKET.get());
                output.accept(ArcticBlitzGatoradeFluid.BUCKET.get());
                output.accept(GrapeGatoradeFluid.BUCKET.get());
                output.accept(StrawberryGatoradeFluid.BUCKET.get());
                output.accept(BlueCherryGatoradeFluid.BUCKET.get());
                output.accept(GreenAppleGatoradeFluid.BUCKET.get());
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
