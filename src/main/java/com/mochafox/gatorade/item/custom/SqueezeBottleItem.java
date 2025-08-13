package com.mochafox.gatorade.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import com.mochafox.gatorade.advancement.util.AdvancementUtil;
import com.mochafox.gatorade.Config;
import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.entity.GatoradeCoolerBlockEntity;
import com.mochafox.gatorade.electrolytes.ElectrolytesUtil;
import com.mochafox.gatorade.fluid.custom.GatoradeFluid;

import javax.annotation.Nonnull;

/**
 * Consumable item representing a squeeze bottle of Gatorade that can hold fluid.
 */
public class SqueezeBottleItem extends Item {
    private static final int CAPACITY = 1000; // mB capacity
    private static final int DRINK_AMOUNT = 250; // mB consumed per use
    
    public SqueezeBottleItem(Item.Properties properties) {
        super(properties);
    }
    
    public static SqueezeBottleItem create(Item.Properties properties) {
        return new SqueezeBottleItem(properties.stacksTo(1)
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
    public InteractionResult use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Fluid block interaction logic
        if (!level.isClientSide && !hasFluid(itemStack)) {

            // Perform a raytrace to see if we're looking at a fluid (similar to empty bucket)
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                return tryFillFromFluidBlock(level, pos, itemStack, player, hand);
            }
        }
        
        // Drinking logic
        if (hasFluid(itemStack)) {
            player.startUsingItem(hand);
            return InteractionResult.SUCCESS;
        } else {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.empty"), true);
            }
            return InteractionResult.FAIL;
        }
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack itemStack, @Nonnull Level level, @Nonnull LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler != null && hasFluid(itemStack)) {
                boolean electrolytesEnabled = Config.ENABLE_ELECTROLYTES.get();
                
                // Get fluid from bottle, subtract drink amount
                int currentAmount = getFluidAmount(itemStack);
                int drinkAmount = Math.min(currentAmount, DRINK_AMOUNT);
                
                // Drain the fluid from the bottle
                FluidStack drained = fluidHandler.drain(drinkAmount, IFluidHandler.FluidAction.EXECUTE);
                
                // Restore electrolytes if enabled
                if (electrolytesEnabled) {
                    ElectrolytesUtil.addElectrolytes(player, drained.getAmount());
                }
                
                // Show feedback to player
                if (electrolytesEnabled) {
                    player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.drank", drained.getAmount()), true);
                }

                // Track advancement progress
                if (player instanceof ServerPlayer serverPlayer) {
                    AdvancementUtil.recordGatoradeConsumption(serverPlayer, drained.getFluid());
                }

                // Return the updated container
                return fluidHandler.getContainer();
            }
        }
        
        return itemStack;
    }

    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack itemStack = context.getItemInHand();
        
        // Only run on server
        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }
        
        // Check if we're clicking on a Gatorade Cooler Block
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof GatoradeCoolerBlockEntity gatoradeCooler) {
            IFluidHandler blockFluidHandler = gatoradeCooler.getFluidHandler();
            IFluidHandlerItem itemFluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            
            if (itemFluidHandler != null) {
                FluidStack blockFluid = blockFluidHandler.getFluidInTank(0);
                FluidStack itemFluid = FluidStack.EMPTY;

                if (hasFluid(itemStack)) {
                    itemFluid = new FluidStack(getFluid(itemStack), getFluidAmount(itemStack));
                }

                if (player.isCrouching() && Config.EMPTYABLE_SQUEEZE_BOTTLE.get()) {
                    return fillBucket(itemFluid, itemFluidHandler, blockFluid, blockFluidHandler, player, hand);
                } else {
                    return fillSqueezeBottle(itemFluid, itemFluidHandler, blockFluid, blockFluidHandler, player, hand);
                }
            }
            
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private InteractionResult tryFillFromFluidBlock(Level level, BlockPos pos, ItemStack itemStack, Player player, InteractionHand hand) {
        FluidState fluidState = level.getFluidState(pos);
        BlockState blockState = level.getBlockState(pos);
        
        Fluid fluid = null;
        boolean isSource = false;
        BlockPos fluidPos = null;
        
        // Try and get fluid from the fluid state or liquid block
        if (!fluidState.isEmpty() && fluidState.isSource()) {
            fluid = fluidState.getType();
            isSource = true;
            fluidPos = pos;
        } else if (blockState.getBlock() instanceof LiquidBlock) {
            FluidState blockFluidState = blockState.getFluidState();
            if (!blockFluidState.isEmpty() && blockFluidState.isSource()) {
                fluid = blockFluidState.getType();
                isSource = true;
                fluidPos = pos;
            }
        }
        
        // Return out if no valid fluid found
        if (fluid == null || !isSource || fluidPos == null || !Gatorade.isGatoradeFluid(fluid)) {
            return InteractionResult.PASS;
        }
        
        IFluidHandlerItem itemFluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (itemFluidHandler == null) {
            return InteractionResult.FAIL;
        }
        
        int currentAmount = getFluidAmount(itemStack);
        Fluid currentFluid = getFluid(itemStack);
        
        // Bottle is full
        if (currentAmount >= CAPACITY) {
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.full"), true);
            return InteractionResult.FAIL;
        }
        
        // Bottle already has different fluid
        if (currentAmount > 0 && currentFluid != fluid) {
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.different_fluid"), true);
            return InteractionResult.FAIL;
        }
        
        // Calculate how much we can fill
        int spaceAvailable = CAPACITY - currentAmount;
        int amountToFill = Math.min(spaceAvailable, 1000); // Fill up to 1 bucket worth
        
        FluidStack fluidToAdd = new FluidStack(fluid, amountToFill);
        int actuallyFilled = itemFluidHandler.fill(fluidToAdd, IFluidHandler.FluidAction.SIMULATE);
        
        if (actuallyFilled > 0) {
            // Remove the fluid block from the world
            level.setBlock(fluidPos, Blocks.AIR.defaultBlockState(), 11);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, fluidPos);
            
            // Fill
            itemFluidHandler.fill(fluidToAdd, IFluidHandler.FluidAction.EXECUTE);

            // Provide feedback to the player
            String fluidName = fluid.getFluidType().getDescription().getString();
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.filled", actuallyFilled, fluidName), true);
            
            // Play pickup sound
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WET_SPONGE_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
            
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS;
    }
    
    private InteractionResult fillSqueezeBottle(FluidStack itemFluid, IFluidHandlerItem itemFluidHandler, FluidStack blockFluid, IFluidHandler blockFluidHandler, Player player, InteractionHand hand) {
        FluidStack toTransfer = blockFluidHandler.drain(CAPACITY, IFluidHandler.FluidAction.SIMULATE);
        int filled = itemFluidHandler.fill(toTransfer, IFluidHandler.FluidAction.SIMULATE);
        if (filled > 0) {
            // Execute the transfer
            FluidStack drained = blockFluidHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
            itemFluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            
            // Update the player's held item with the new container from the fluid handler
            player.setItemInHand(hand, itemFluidHandler.getContainer());
            
            // Provide feedback to the player
            String fluidName = drained.getFluid().getFluidType().getDescription().getString();
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.filled", filled, fluidName), true);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WET_SPONGE_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResult.SUCCESS;
        } else if (itemFluid.getAmount() >= CAPACITY) {
            // Bottle is already full
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.full"), true);
        } else if (itemFluid.getAmount() < CAPACITY && blockFluid.isEmpty()) {
            // Cooler is empty and bottle is not full
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.cooler.empty"), true);
        } else if (!FluidStack.isSameFluid(itemFluid, blockFluid)) {
            // Bottle contains a different fluid than the cooler
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.different_fluid"), true);
        } else {
            // Cannot fill the bottle for some reason
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.bottle.cannot_fill"), true);
        }

        return InteractionResult.FAIL;   
    }

    private InteractionResult fillBucket(FluidStack itemFluid, IFluidHandlerItem itemFluidHandler, FluidStack blockFluid, IFluidHandler blockFluidHandler, Player player, InteractionHand hand) {
        if (!itemFluid.isEmpty()) {
            int filled = blockFluidHandler.fill(itemFluid, IFluidHandler.FluidAction.SIMULATE);
            if (filled > 0) {
                // Execute the transfer
                blockFluidHandler.fill(itemFluid.copyWithAmount(filled), IFluidHandler.FluidAction.EXECUTE);
                itemFluidHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                
                // Update the player's held item with the new container from the fluid handler
                player.setItemInHand(hand, itemFluidHandler.getContainer());
                
                // Provide feedback to the player
                String fluidName = itemFluid.getFluid().getFluidType().getDescription().getString();
                player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.emptied", filled, fluidName), true);
                
                return InteractionResult.SUCCESS;
            } else {
                // Bucket is full or incompatible fluid
                if (blockFluid.isEmpty() || FluidStack.isSameFluidSameComponents(blockFluid, itemFluid)) {
                    player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.cooler_full"), true);
                } else {
                    player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.different_fluid"), true);
                }
                return InteractionResult.FAIL;
            }
        } else {
            player.displayClientMessage(Component.translatable("item.gatorade.squeeze_bottle.empty"), true);
            return InteractionResult.FAIL;
        }
    }
    
    // === Fluid Management === //
    private boolean hasFluid(ItemStack itemStack) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return false;
        }
        return !fluidHandler.getFluidInTank(0).isEmpty();
    }
    
    private int getFluidAmount(ItemStack itemStack) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return 0;
        }
        return fluidHandler.getFluidInTank(0).getAmount();
    }

    private Fluid getFluid(ItemStack itemStack) {
        IFluidHandlerItem fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return Fluids.EMPTY;
        }
        return fluidHandler.getFluidInTank(0).getFluid();
    }

    // === Fluid Bar === //
    @Override
    public boolean isBarVisible(@Nonnull ItemStack itemStack) {
        return hasFluid(itemStack);
    }
    
    @Override
    public int getBarWidth(@Nonnull ItemStack itemStack) {
        if (!hasFluid(itemStack)) {
            return 0;
        }
        int amount = getFluidAmount(itemStack);
        return Math.round(13.0F * amount / CAPACITY);
    }
    
    @Override
    public int getBarColor(@Nonnull ItemStack itemStack) {
        Fluid fluid = getFluid(itemStack);
        if(fluid instanceof GatoradeFluid) {
            GatoradeFluid gatoradeFluid = (GatoradeFluid) fluid;
            int tintColor = gatoradeFluid.getTintColor();
            return ARGB.opaque(tintColor);
        }

        return ARGB.opaque(Gatorade.DEFAULT_FLUID_COLOR);
    }
}
