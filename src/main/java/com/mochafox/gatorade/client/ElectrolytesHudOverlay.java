package com.mochafox.gatorade.client;

import com.mochafox.gatorade.Gatorade;
import com.mochafox.gatorade.Config;
import com.mochafox.gatorade.electrolytes.ElectrolytesUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

/**
 * HUD overlay that renders the electrolytes bar above the food bar.
 */
public class ElectrolytesHudOverlay implements GuiLayer {
    private static final int ICON_SIZE = 9;
    
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        
        // Check if electrolytes system is enabled
        if (!Config.ENABLE_ELECTROLYTES.get()) {
            return;
        }
        
        // Only render in survival/adventure mode and when not in spectator
        if (!minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer() && minecraft.player != null) {
            Player player = minecraft.player;
            
            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();
            
            // Position the electrolytes bar above the food bar
            // Food bar is at (screenWidth / 2 + 10, screenHeight - 39)
            // So we position electrolytes bar 12 pixels above it
            int x = screenWidth / 2 + 10;
            int y = screenHeight - 51; // 39 + 12 = 51
            
            renderElectrolytesBar(guiGraphics, player, x, y);
        }
    }
    
    /**
     * Renders the electrolytes bar at the specified position.
     */
    private void renderElectrolytesBar(GuiGraphics guiGraphics, Player player, int x, int y) {
        float electrolytePercentage = ElectrolytesUtil.getElectrolytePercentage(player);
        boolean isLow = ElectrolytesUtil.areElectrolytesLow(player);
        boolean isCritical = ElectrolytesUtil.areElectrolytesCritical(player);
        
        // Render background icons (empty electrolyte icons)
        for (int i = 0; i < 10; i++) {
            int iconX = x + i * 8;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getElectrolyteIcon(false, false, false, false), iconX, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }

        int fullIcons = Mth.floor(electrolytePercentage * 10);
        boolean hasHalfIcon = (electrolytePercentage * 10) % 1 >= 0.5f;
        
        // Render filled icons
        for (int i = 0; i < fullIcons; i++) {
            int iconX = x + i * 8;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getElectrolyteIcon(true, isLow, isCritical, false), iconX, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }
        
        // Render half icon if needed
        if (hasHalfIcon && fullIcons < 10) {
            int iconX = x + fullIcons * 8;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getElectrolyteIcon(true, isLow, isCritical, true), iconX, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }
    }

    /**
     * Gets the appropriate texture resource location for electrolyte icons.
     */
    private ResourceLocation getElectrolyteIcon(boolean filled, boolean isLow, boolean isCritical, boolean half) {
        String iconName;
        
        if (!filled) {
            iconName = "electrolyte_empty";
        } else if (half) {
            if (isCritical) {
                iconName = "electrolyte_half_full_critical";
            } else if (isLow) {
                iconName = "electrolyte_half_full_low";
            } else {
                iconName = "electrolyte_half_full";
            }
        } else {
            if (isCritical) {
                iconName = "electrolyte_full_critical";
            } else if (isLow) {
                iconName = "electrolyte_full_low";
            } else {
                iconName = "electrolyte_full";
            }
        }
        
        // TODO: migrate to atlas
        return ResourceLocation.fromNamespaceAndPath(Gatorade.MODID, "textures/gui/electrolytes/" + iconName + ".png");
    }
}
