package com.sarakhman.onlineStore.model;

public enum Status {
    UNREGISTERED("Unregistered"),
    REGISTERED("Registered"),
    PAID("Paid"),
    CANCELED("Canceled");

    private final String displayValue;

    private Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
