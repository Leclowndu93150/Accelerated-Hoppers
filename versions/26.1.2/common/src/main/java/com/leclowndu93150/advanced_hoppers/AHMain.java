package com.leclowndu93150.advanced_hoppers;

import net.minecraft.resources.Identifier;

public final class AHMain {
    public static final String MODID = "advanced_hoppers";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
