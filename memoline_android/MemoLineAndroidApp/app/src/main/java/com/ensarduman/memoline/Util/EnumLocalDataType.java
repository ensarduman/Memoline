package com.ensarduman.memoline.Util;

/**
 * Created by duman on 13/02/2018.
 */

public enum EnumLocalDataType {
    SQLite(1);

    private final int value;
    EnumLocalDataType(final int val) {
        this.value = val;
    }

    public int getValue()
    {
        return this.value;
    }
}
