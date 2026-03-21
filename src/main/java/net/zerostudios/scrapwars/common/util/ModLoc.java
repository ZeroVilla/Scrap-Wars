package net.zerostudios.scrapwars.common.util;

import net.minecraft.resources.ResourceLocation;

public final class ModLoc {

    private ModLoc() {
    }

    public static ResourceLocation of(String path) {
        return new ResourceLocation(ModIds.MOD_ID, path);
    }
}
