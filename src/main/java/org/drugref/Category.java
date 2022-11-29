package org.drugref;

public enum Category {
    BRAND(13),
    COMPOSITE_GENERIC(12),
    GENERIC(11),
    ATC(8),
    AHFS(10),
    ACTIVE_INGREDIENT(14),
    AI_COMPOSITE_GENERIC(19),
    AI_GENERIC(18);

    private int ordinal;

    Category(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}
