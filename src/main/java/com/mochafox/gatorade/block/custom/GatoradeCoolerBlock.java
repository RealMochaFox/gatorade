package com.mochafox.gatorade.block.custom;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.block.entity.GatoradeCoolerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Block representing a placed Gatorade cooler with fluid storage capability.
 */
public class GatoradeCoolerBlock extends BaseEntityBlock implements LiquidBlockContainer {
    public static final MapCodec<GatoradeCoolerBlock> CODEC = simpleCodec(GatoradeCoolerBlock::new);

    public GatoradeCoolerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new GatoradeCoolerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity entity, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        if (!Gatorade.isGatoradeFluid(fluid)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof GatoradeCoolerBlockEntity)) {
            return false;
        }

        IFluidHandler blockFluidHandler = ((GatoradeCoolerBlockEntity) blockEntity).getFluidHandler();
        FluidStack fluidInBucket = new FluidStack(fluid, Gatorade.BUCKET_AMOUNT);

        return blockFluidHandler.isFluidValid(0, fluidInBucket) && 
               blockFluidHandler.fill(fluidInBucket, IFluidHandler.FluidAction.SIMULATE) > 0;
    }

    @Override
    public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull FluidState fluidState) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof GatoradeCoolerBlockEntity)) {
            return false;
        }

        IFluidHandler blockFluidHandler = ((GatoradeCoolerBlockEntity) blockEntity).getFluidHandler();
        FluidStack fluidInBucket = new FluidStack(fluidState.getType(), Gatorade.BUCKET_AMOUNT);

        int filled = blockFluidHandler.fill(fluidInBucket, IFluidHandler.FluidAction.EXECUTE);
        return filled > 0;
    }

    @Override
    protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                                          @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Item item = stack.getItem();
        if (!(item instanceof BucketItem bucketItem)) {
            return InteractionResult.PASS;
        }

        // Handle empty bucket - extract from cooler
        if (bucketItem.content == Fluids.EMPTY) {
            return fillBucket(level, pos, stack, player, hand);
        } else {
            // Handle filled bucket - empty into cooler
            return emptyBucket(level, pos, stack, player, hand, bucketItem.content);
        }
    }

    private InteractionResult emptyBucket(Level level, BlockPos pos, ItemStack stack, Player player, InteractionHand hand, Fluid fluid) {
        if (!canPlaceLiquid(player, level, pos, level.getBlockState(pos), fluid)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof GatoradeCoolerBlockEntity) {
                IFluidHandler blockFluidHandler = ((GatoradeCoolerBlockEntity) blockEntity).getFluidHandler();
                FluidStack currentFluid = blockFluidHandler.getFluidInTank(0);
                
                if (!currentFluid.isEmpty() && !FluidStack.isSameFluidSameComponents(currentFluid, new FluidStack(fluid, Gatorade.BUCKET_AMOUNT))) {
                    // Different fluids
                    player.displayClientMessage(Component.translatable("block.gatorade.gatorade_cooler_block.different_fluid"), true);
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.FAIL;
        }

        // Place the liquid
        if (placeLiquid(level, pos, level.getBlockState(pos), fluid.defaultFluidState())) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            
            if (!player.hasInfiniteMaterials()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, emptyBucket);
                } else if (!player.getInventory().add(emptyBucket)) {
                    player.drop(emptyBucket, false);
                }
            }
            
            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.FAIL;
    }

    private InteractionResult fillBucket(Level level, BlockPos pos, ItemStack stack, Player player, InteractionHand hand) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof GatoradeCoolerBlockEntity)) {
            return InteractionResult.PASS;
        }

        IFluidHandler blockFluidHandler = ((GatoradeCoolerBlockEntity) blockEntity).getFluidHandler();
        
        // Check if the cooler contains enough fluid to fill a bucket
        FluidStack currentFluid = blockFluidHandler.getFluidInTank(0);
        if (currentFluid.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.gatorade.gatorade_cooler_block.empty"), true);
            return InteractionResult.FAIL;
        }

        if (currentFluid.getAmount() < Gatorade.BUCKET_AMOUNT) {
            player.displayClientMessage(Component.translatable("block.gatorade.gatorade_cooler_block.not_enough_fluid_for_bucket", currentFluid.getAmount(), Gatorade.BUCKET_AMOUNT), true);
            return InteractionResult.FAIL;
        }

        // Require chaos mode to extract non-Gatorade fluids
        Fluid fluid = currentFluid.getFluid();
        if (!Gatorade.isGatoradeFluid(fluid)) {
            player.displayClientMessage(Component.translatable("block.gatorade.gatorade_cooler_block.contains_invalid_fluid", fluid.toString()), true);
            return InteractionResult.FAIL;
        }

        // Create filled bucket
        ItemStack filledBucket = new ItemStack(fluid.getBucket());

        // Drain the fluid from the cooler
        blockFluidHandler.drain(Gatorade.BUCKET_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
        
        // Handle inventory management
        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
            
            if (stack.isEmpty()) {
                player.setItemInHand(hand, filledBucket);
            } else if (!player.getInventory().add(filledBucket)) {
                // If the inventory is full, drop the filled bucket
                player.drop(filledBucket, false);
            }
        } else {
            if (!player.getInventory().add(filledBucket)) {
                player.drop(filledBucket, false);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
