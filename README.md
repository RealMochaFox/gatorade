# Gatorade Mod

## ⚠️ Legal Disclaimer

**This mod is an unofficial, parody project for educational and entertainment purposes only.**

- **NOT AFFILIATED:** This project is not affiliated with, endorsed by, or sponsored by PepsiCo, Inc. or the Gatorade brand
- **TRADEMARK NOTICE:** "Gatorade" is a registered trademark of PepsiCo, Inc.
- **FAIR USE:** This mod is protected under fair use and parody provisions
- **TRANSFORMATIVE WORK:** This is a parody that transforms the original concept into a gaming context

## About

A NeoForge mod for Minecraft that adds Gatorade as a naturally occurring resource to the world. Find gatorade springs, fill your gatorade bucket and drink from a gatorade squeeze bottle.

## Features

### Gatorade Fluids & Items
- **14 Unique Gatorade Flavors**: Including Orange, Cool Blue, Lemon-Lime, Fruit Punch, Grape, Strawberry, Glacier Freeze, Arctic Blitz, Lightning Blast, and more
- **Squeeze Bottles**: Reusable 1000mB capacity bottles that can be filled, consumed, and refilled
  - Visual fluid level indicator bar
  - Color-coded based on contained fluid
  - Restores electrolytes when consumed (250mB per drink)
- **Gatorade Buckets**: Standard 1000mB buckets for each fluid type
- **Infinite Electrolyte Drink**: Creative-only item for testing and convenience

### Electrolytes System
- **Dynamic Electrolyte Bar**: HUD overlay above the food bar showing current electrolyte levels
- **Electrolyte Effects**: 
  - **High levels (80-100%)**: Speed, Haste, and Jump Boost effects
  - **Medium levels (30-70%)**: No effects
  - **Low levels (20-30%)**: Slowness and Mining Fatigue
  - **Critical levels (0-20%)**: Severe debuffs including Hunger
- **Natural Decay**: Electrolytes decrease over time, movement, and sprinting
- **Electrolyte Restoration**: Drinking Gatorade fluids restores electrolytes

### Blocks & Storage
- **Gatorade Cooler Block**: Large fluid storage container (16,000mB capacity)
  - Accepts any Gatorade fluid type
  - Compatible with fluid pipes and standard containers
  - Can be filled/emptied with buckets and squeeze bottles

### Configuration Options
- **Electrolytes System**: Enable/disable the entire electrolytes mechanic
- **Decay Rates**: Configurable decay rates for time, movement, and sprinting
- **Chaos Mode**: Allow fluid containers to accept any fluid type (not just Gatorade)
- **Emptyable Squeeze Bottles**: Allow emptying bottles back into bucket blocks
- **Max/Default Electrolytes**: Customizable electrolyte limits and starting values

### Achievements & Progression
- **Advancement System**: Achievement tree tracking your Gatorade journey

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
