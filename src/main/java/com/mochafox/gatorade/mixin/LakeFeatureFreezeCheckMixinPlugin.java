package com.mochafox.gatorade.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.mochafox.gatorade.Gatorade;

import java.util.List;
import java.util.Set;

public class LakeFeatureFreezeCheckMixinPlugin implements IMixinConfigPlugin {

    private static boolean lakeFixPresent;

    static {
        try {
            lakeFixPresent =
                net.neoforged.fml.loading.FMLLoader.getLoadingModList().getModFileById("lakefeaturefix") != null;
        } catch (Throwable t) {
            lakeFixPresent = false;
        }
    }

    @Override public void onLoad(String mixinPackage) {}

    @Override public String getRefMapperConfig() { return null; }

    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // If a known lake-fix mod is present, skip our patch to avoid double-patching.
        boolean isLakeFixMixin = mixinClassName.endsWith("LakeFeatureFreezeCheckRedirectMixin");
        if (lakeFixPresent && isLakeFixMixin) {
            return false;
        } else if (isLakeFixMixin) {
            Gatorade.LOGGER.info("Patching lake crash via " + mixinClassName);
        }

        return true;
    }

    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
