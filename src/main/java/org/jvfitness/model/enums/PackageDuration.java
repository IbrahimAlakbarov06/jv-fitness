package org.jvfitness.model.enums;

public enum PackageDuration {
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    YEAR_MONTH(12);

    private final int months;

    PackageDuration(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }
}
