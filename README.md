# Gatorade Mod

## ⚠️ Legal Disclaimer

**This mod is an unofficial, parody project for educational and entertainment purposes only.**

- **NOT AFFILIATED:** This project is not affiliated with, endorsed by, or sponsored by PepsiCo, Inc. or the Gatorade brand
- **TRADEMARK NOTICE:** "Gatorade" is a registered trademark of PepsiCo, Inc.
- **FAIR USE:** This mod is protected under fair use and parody provisions
- **TRANSFORMATIVE WORK:** This is a parody that transforms the original concept into a gaming context

## About

A NeoForge mod for Minecraft that adds Gatorade as a naturally occurring resource to the world. Find gatorade lakes, fill your gatorade bucket and drink from a gatorade squeeze bottle.

## Features

### Gatorade Fluids & Items
- **14 Unique Gatorade Flavors**:
  - Orange Gatorade
  - Cool Blue Gatorade
  - Lemon-Lime Gatorade
  - Fruit Punch Gatorade
  - Grape Gatorade
  - Strawberry Gatorade
  - Glacier Freeze Gatorade
  - Arctic Blitz Gatorade
  - Lightning Blast Gatorade
  - Lime Cucumber Gatorade
  - Blue Cherry Gatorade
  - Green Apple Gatorade
  - Glacier Cherry Gatorade
  - Midnight Ice Gatorade
- **Squeeze Bottles**: Reusable 1000mB capacity bottles that can be filled, consumed, and refilled
  - Visual fluid level indicator bar
  - Color-coded based on contained fluid
  - Restores electrolytes when consumed (250mB per drink)
  - Crafting recipe available
- **Gatorade Buckets**: Standard 1000mB buckets for each fluid type
- **Infinite Electrolyte Drink**: Creative-only item that fully refills electrolyte meter and never runs out

### Electrolytes System
- **Dynamic Electrolyte Bar**: HUD overlay above the food bar showing current electrolyte levels
- **Survival Mode Only**: Electrolyte system only active in survival mode
- **Regenerative Fluid Behavior**: Refills when standing in Gatorade fluid
- **Detailed Electrolyte Effects**:
  - **Stage 3 (95-100%)**: Haste II + Speed II + Jump Boost II
  - **Stage 2 (80-95%)**: Haste I + Speed I + Jump Boost I
  - **Stage 1 (80-90%)**: Haste I
  - **Stage 0 (30-70%)**: No effects
  - **Stage -1 (20-30%)**: Slowness I + Mining Fatigue I
  - **Stage -2 (10-20%)**: Slowness II + Mining Fatigue II
  - **Stage -3 (1-10%)**: Slowness III + Mining Fatigue III + Hunger I
  - **Stage -4 (0-1%)**: Slowness III + Mining Fatigue III + Hunger II
- **Natural Decay**: Electrolytes decrease through multiple methods:
  - 1 electrolyte every 600 ticks (time decay)
  - 1 electrolyte per movement action
  - 2 electrolytes when sprinting
  - Additional decay based on jumping

### Blocks & Storage
- **Gatorade Cooler Block**: Large fluid storage container (16,000mB capacity)
  - Themed like a real Gatorade cooler
  - Accepts any Gatorade fluid type
  - Compatible with fluid pipes and standard containers
  - Can be filled/emptied with buckets and squeeze bottles
  - Crafting recipe available
  - Supports filling from reusable squeeze bottles in chaos mode

### World Generation
- **Gatorade Lakes**: Naturally occurring Gatorade lakes in all overworld biomes
  - Rare generation
  - Contains various Gatorade flavors
  - Fix for lake crash in 1.21.x

### Configuration Options
- **Electrolytes System**: Enable/disable the entire electrolytes mechanic
- **Decay Rates**: Configurable decay rates for:
  - Tick decay rate (time-based)
  - Sprint decay rate
  - Jump decay rate
  - Movement decay rate
- **Chaos Mode**: Allow fluid containers to accept any fluid type (not just Gatorade)
- **Emptyable Squeeze Bottles**: Allow emptying bottles back into cooler blocks
- **Max/Default Electrolytes**: Customizable electrolyte limits and starting values
- **Regenerative Physical Fluid**: Configure rate and amount for fluid-based regeneration

### Achievements & Progression
- **Comprehensive Advancement System**: Achievement tree tracking your Gatorade journey
  - Drink milestones: 10, 25, 50, 75, 100, 500, 1000, 10000 bottles
  - Flavor-based achievements:
    - Drink first Gatorade flavor
    - Drink a new flavor of Gatorade
    - Drink all Gatorade flavors
  - Equipment achievements:
    - Obtain a cooler
    - Obtain a squeeze bottle

### Chaos Mode
- **Universal Fluid Support**: When enabled, allows drinking and storing all fluids in the game
- **Enhanced Compatibility**: Fluid containers work with any liquid, not just Gatorade

### Commands
- **`/electrolytes` command**: Admin commands to get, set, and add electrolytes for testing

## Installation

1. Ensure you have Minecraft 1.21+ installed
2. Download and install NeoForge 21.7.23-beta or later
3. Download the latest mod JAR from the Releases page
4. Place the JAR file in your Minecraft `mods` folder

## Development

### Requirements

- Java 21 or later
- Minecraft 1.21+
- NeoForge 21.7.23-beta or later

### Building

```bash
# Install dependencies (if using asdf)
asdf install

# Build the mod
./gradlew build
```

### Development Environment

Recommended VSCode extensions are listed in `.vscode/extensions.json`.
