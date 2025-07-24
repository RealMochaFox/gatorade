package com.mochafox.gatorade.commands;

import com.mochafox.gatorade.Config;
import com.mochafox.gatorade.electrolytes.ElectrolytesUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Commands for testing and managing electrolytes.
 */
public class ElectrolytesCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("electrolytes")
            .requires(source -> source.hasPermission(2)) // Requires OP level 2
            .then(Commands.literal("get")
                .executes(context -> getElectrolytes(context, context.getSource().getPlayerOrException()))
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> getElectrolytes(context, EntityArgument.getPlayer(context, "player")))))
            .then(Commands.literal("set")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, Config.MAX_ELECTROLYTES.get()))
                    .executes(context -> setElectrolytes(context, context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "amount")))
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> setElectrolytes(context, EntityArgument.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount"))))))
            .then(Commands.literal("add")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                    .executes(context -> addElectrolytes(context, context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "amount")))
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> addElectrolytes(context, EntityArgument.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount"))))))
        );
    }
    
    private static int getElectrolytes(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        if (!Config.ENABLE_ELECTROLYTES.get()) {
            Component message = Component.translatable("gatorade.commands.electrolytes.disabled");
            context.getSource().sendFailure(message);
            return 0;
        }
        
        int electrolytes = ElectrolytesUtil.getElectrolyteLevel(player);
        float percentage = ElectrolytesUtil.getElectrolytePercentage(player);
        
        Component message = Component.translatable("gatorade.commands.electrolytes.get.result", 
            player.getName().getString(), electrolytes, Config.MAX_ELECTROLYTES.get(), percentage * 100);
        
        context.getSource().sendSuccess(() -> message, false);
        return electrolytes;
    }
    
    private static int setElectrolytes(CommandContext<CommandSourceStack> context, ServerPlayer player, int amount) {
        if (!Config.ENABLE_ELECTROLYTES.get()) {
            Component message = Component.translatable("gatorade.commands.electrolytes.disabled");
            context.getSource().sendFailure(message);
            return 0;
        }
        
        ElectrolytesUtil.setElectrolyteLevel(player, amount);
        
        Component message = Component.translatable("gatorade.commands.electrolytes.set.success", 
            player.getName().getString(), amount);
        
        context.getSource().sendSuccess(() -> message, true);
        return amount;
    }
    
    private static int addElectrolytes(CommandContext<CommandSourceStack> context, ServerPlayer player, int amount) {
        if (!Config.ENABLE_ELECTROLYTES.get()) {
            Component message = Component.translatable("gatorade.commands.electrolytes.disabled");
            context.getSource().sendFailure(message);
            return 0;
        }
        
        int oldAmount = ElectrolytesUtil.getElectrolyteLevel(player);
        ElectrolytesUtil.addElectrolytes(player, amount);
        int newAmount = ElectrolytesUtil.getElectrolyteLevel(player);
        
        Component message = Component.translatable("gatorade.commands.electrolytes.add.success", 
            amount, player.getName().getString(), oldAmount, newAmount);
        
        context.getSource().sendSuccess(() -> message, true);
        return newAmount;
    }
}
