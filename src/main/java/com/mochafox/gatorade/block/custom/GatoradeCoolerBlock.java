package com.mochafox.gatorade.block.custom;

import com.mochafox.gatorade.block.entity.GatoradeCoolerBlockEntity;
import com.mochafox.gatorade.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

/**
 * Block representing a placed Gatorade cooler with fluid storage capability.
 */
public class GatoradeCoolerBlock extends BaseEntityBlock {
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
    protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                                          @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hitResult) {
        // Only run on server
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        // Check if the item is a bucket
        if (stack.getItem() instanceof net.minecraft.world.item.BucketItem) {
            // Try to get the fluid handler for the bucket
            IFluidHandler itemFluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (itemFluidHandler != null && !itemFluidHandler.getFluidInTank(0).isEmpty()) {
                // Get the block entity and its fluid handler
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GatoradeCoolerBlockEntity gatoradeCooler) {
                    IFluidHandler blockFluidHandler = gatoradeCooler.getFluidHandler();
                    FluidStack fluidInBucket = itemFluidHandler.getFluidInTank(0);
                    
                    // Try to fill the block with the fluid from the bucket
                    int filled = blockFluidHandler.fill(fluidInBucket, IFluidHandler.FluidAction.EXECUTE);
                    if (filled > 0) {
                        // Drain the fluid from the bucket and give the player an empty bucket
                        itemFluidHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                        // Replace the player's held item with an empty Gatorade bucket
                        player.setItemInHand(hand, new ItemStack(ModItems.GATORADE_BUCKET.get()));

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
