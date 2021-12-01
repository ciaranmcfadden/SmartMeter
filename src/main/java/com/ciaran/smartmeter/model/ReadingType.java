package com.ciaran.smartmeter.model;

public enum ReadingType {
    GAS_READING("G"),
    ELEC_READING("E");

    public final String type;

    private ReadingType(String type) {
        this.type = type;
    }
}
