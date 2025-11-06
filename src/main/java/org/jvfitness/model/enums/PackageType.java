package org.jvfitness.model.enums;

public enum PackageType {
    ENTRIES_12(12),
    ENTRIES_31(31),
    UNLIMITED(0);

    private final int maxEntries;

    PackageType(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public boolean isUnlimited() {
        return this == UNLIMITED;
    }

}
