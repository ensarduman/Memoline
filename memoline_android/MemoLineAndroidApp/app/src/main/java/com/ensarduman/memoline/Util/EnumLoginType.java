package com.ensarduman.memoline.Util;

/**
 * Created by duman on 12/02/2018.
 *
 * Not içeriğinin tipi
 */

public enum EnumLoginType {

    /**
     * Basic
     */
    Basic(1),

    /**
     * Facebook
     */
    Facebook(2);

    private final int value;

    EnumLoginType(final int newValue){
        value = newValue;
    }

    public  int getValue(){
        return  value;
    }

    public static EnumLoginType fromInt(int newValue)
    {
        if(newValue == 1)
            return EnumLoginType.Basic;
        if(newValue == 2)
            return EnumLoginType.Facebook;
        else
            return null;
    }
}
