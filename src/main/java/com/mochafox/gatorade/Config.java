package com.mochafox.gatorade;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Fluid & Containers Section
    public static final ModConfigSpec.BooleanValue CHAOS_MODE;
    public static final ModConfigSpec.BooleanValue EMPTYABLE_SQUEEZE_BOTTLE;

    // Electrolytes Section
    public static final ModConfigSpec.BooleanValue ENABLE_ELECTROLYTES;
    public static final ModConfigSpec.IntValue ELECTROLYTES_DECAY_RATE;
    public static final ModConfigSpec.IntValue ELECTROLYTES_SPRINT_DECAY_RATE;
    public static final ModConfigSpec.IntValue ELECTROLYTES_MOVE_DECAY_RATE;
    public static final ModConfigSpec.IntValue MAX_ELECTROLYTES;
    public static final ModConfigSpec.IntValue DEFAULT_ELECTROLYTES;
    public static final ModConfigSpec.BooleanValue GATORADE_BATHING_REGENERATION_ELECTROLYTE_EFFECTS;
    public static final ModConfigSpec.BooleanValue GATORADE_BATHING_REGENERATION_PHYSICAL_EFFECTS;
    public static final ModConfigSpec.IntValue GATORADE_BATHING_REGENERATION_RATE;
    public static final ModConfigSpec.IntValue GATORADE_BATHING_REGENERATION_AMOUNT;

    static {

        BUILDER.comment("Fluid & Containers").push("fluidContainers");

        CHAOS_MODE = BUILDER
                .comment("Allow filling of Gatorade containers with any liquid, not just Gatorade fluids.")
                .define("chaosMode", false);

        EMPTYABLE_SQUEEZE_BOTTLE = BUILDER
                .comment("Allow emptying squeeze bottles into Gatorade buckets.")
                .define("emptyableSqueezeBottle", false);

        BUILDER.pop();

        BUILDER.comment("Electrolytes System").push("electrolytes");

        ENABLE_ELECTROLYTES = BUILDER
                .comment("Enable the electrolytes system that tracks player hydration and applies effects.")
                .define("enableElectrolytes", true);

        ELECTROLYTES_DECAY_RATE = BUILDER
                .comment("Rate at which electrolytes decay over time, in ticks (20 ticks = 1 second).")
                .defineInRange("electrolytesDecayRate", 600, 100, 1200); // Default 30 seconds, range 5-60 seconds

        ELECTROLYTES_SPRINT_DECAY_RATE = BUILDER
                .comment("Rate at which electrolytes decay while sprinting, in ticks (20 ticks = 1 second).")
                .defineInRange("electrolytesSprintDecayRate", 300, 100, 600); // Default 15 seconds, range 5-30 seconds

        ELECTROLYTES_MOVE_DECAY_RATE = BUILDER
                .comment("Rate at which electrolytes decay while moving, in ticks (20 ticks = 1 second).")
                .defineInRange("electrolytesMoveDecayRate", 400, 100, 800); // Default 20 seconds, range 5-40 seconds

        MAX_ELECTROLYTES = BUILDER
                .comment("Maximum electrolytes a player can have.")
                .defineInRange("maxElectrolytes", 1000, 100, 1000); // Default 1000, range 100-1000

        DEFAULT_ELECTROLYTES = BUILDER
                .comment("Default electrolytes level when a player joins.")
                .defineInRange("defaultElectrolytes", 1000, 100, 1000); // Default 1000, range 100-1000

        GATORADE_BATHING_REGENERATION_ELECTROLYTE_EFFECTS = BUILDER
                .comment("Enable regeneration of electrolytes while in Gatorade.")
                .define("gatoradeBathingRegenerationElectrolyteEffects", true);

        GATORADE_BATHING_REGENERATION_PHYSICAL_EFFECTS = BUILDER
                .comment("Enable physical effects while in Gatorade.")
                .define("gatoradeBathingRegenerationPhysicalEffects", true);

        GATORADE_BATHING_REGENERATION_RATE = BUILDER
                .comment("Rate at which players regenerate electrolytes while in Gatorade.")
                .defineInRange("gatoradeBathingRegenerationRate", 600, 1, 1200);

        GATORADE_BATHING_REGENERATION_AMOUNT = BUILDER
                .comment("Amount of electrolytes restored per tick while in Gatorade.")
                .defineInRange("gatoradeBathingRegenerationAmount", 80, 0, 1000);

        BUILDER.pop();
    }

    static final ModConfigSpec SPEC = BUILDER.build();
}
