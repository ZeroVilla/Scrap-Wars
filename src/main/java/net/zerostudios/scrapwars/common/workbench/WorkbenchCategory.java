package net.zerostudios.scrapwars.common.workbench;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum WorkbenchCategory {
    ALL("all", "gui.scrapwars.category.all"),
    WEAPONS("weapons", "gui.scrapwars.category.weapons"),
    TOOLS("tools", "gui.scrapwars.category.tools"),
    ELECTRONICS("electronics", "gui.scrapwars.category.electronics"),
    COMPONENTS("components", "gui.scrapwars.category.components"),
    MATERIALS("materials", "gui.scrapwars.category.materials");

    private final String id;
    private final String langKey;

    WorkbenchCategory(String id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public String getId() {
        return id;
    }

    public String getLangKey() {
        return langKey;
    }

    public MutableComponent getTranslatedName() {
        return Component.translatable(this.langKey);
    }

    public static WorkbenchCategory fromId(String id) {
        for (WorkbenchCategory category : values()) {
            if (category.id.equals(id)) {
                return category;
            }
        }
        return ALL;
    }
}