package com.mochafox.gatorade.item.custom;

import com.mochafox.gatorade.Config;
import com.mochafox.gatorade.electrolytes.ElectrolytesUtil;

import javax.annotation.Nonnull;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;

/**
 * Infinite electrolyte drink item that doesnt run out and fully restores player electrolytes when consumed.
 * This is intended as a creative-only item for testing and convenience.
 */
public class InfiniteElectrolyteDrinkItem extends Item {
    public InfiniteElectrolyteDrinkItem(Properties properties) {
        super(properties);
    }

    public static InfiniteElectrolyteDrinkItem create(Item.Properties properties) {
        return new InfiniteElectrolyteDrinkItem(properties.stacksTo(1)
            .food(new FoodProperties.Builder()
                .alwaysEdible()
                .nutrition(1)
                .saturationModifier(0.5f)
                .build())
            .component(DataComponents.CONSUMABLE, 
                Consumable.builder()
                    .consumeSeconds(1.6f)
                    .animation(ItemUseAnimation.DRINK)
                    .sound(SoundEvents.GENERIC_DRINK)
                    .build()));
    }
    
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity livingEntity) {
        super.finishUsingItem(stack, level, livingEntity);
        
        if (livingEntity instanceof Player player) {
            if (Config.ENABLE_ELECTROLYTES.get()) {
                int electrolytesRestored = Config.MAX_ELECTROLYTES.get();

                // Restore electrolytes
                ElectrolytesUtil.addElectrolytes(player, electrolytesRestored);

                // Show feedback to player
                player.displayClientMessage(Component.translatable("item.gatorade.infinite_electrolyte_drink.drank.electrolytes"), true);
            }
        }
        
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return stack;
        }
    }

    
    @Override
    public InteractionResult use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResult.SUCCESS;
    }
}
