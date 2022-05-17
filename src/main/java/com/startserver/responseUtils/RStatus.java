package com.startserver.responseUtils;

/**
 * Created by anton on 06.11.2017.
 */
public enum RStatus {

    ERROR (600),
    FATAL_ERROR (650);

    final int value;

    RStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
