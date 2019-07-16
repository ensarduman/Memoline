package com.ensarduman.memoline.Util;

/**
 * Created by duman on 18/03/2018.
 */



public enum EnumResponseStatus {
    Success(1),
    Error(2),
    LoginError(3),
    UserIsNotValid(4),
    AuthorizationError(5),
    NotFoundError(6),
    UserAlreadyExists(7);

    private final int value;
    EnumResponseStatus(final int val) {
        this.value = val;
    }

    public int getValue()
    {
        return this.value;
    }
}
