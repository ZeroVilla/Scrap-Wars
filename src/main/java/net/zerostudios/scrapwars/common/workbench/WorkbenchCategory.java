package net.zerostudios.scrapwars.common.workbench;

public enum WorkbenchCategory {
    ALL("all"),
    WEAPONS("weapons"),
    TOOLS("tools"),
    ELECTRONICS("electronics"),
    COMPONENTS("components"),
    MATERIALS("materials");

    private final String id;

    WorkbenchCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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