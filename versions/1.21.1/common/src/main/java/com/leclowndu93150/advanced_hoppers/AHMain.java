package com.leclowndu93150.advanced_hoppers;

import net.minecraft.resources.ResourceLocation;

public final class AHMain {
    public static final String MODID = "advanced_hoppers";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
