package com.ensarduman.memoline.Util;

/**
 * Created by duman on 12/02/2018.
 *
 * Not içeriğinin tipi
 */

public enum EnumNoteType {

    /**
     * Yazı tipindeki içerikler
     */
    Text(1),

    /**
     * Resim tipindeki içerikler
     */
    Image(2),

    /**
     * Video tipindeki içerikler
     */
    Video(3),

    /**
     * GPS tipindeki içerikler
     */
    GPS(4);

    private final int value;

    EnumNoteType(final int newValue){
        value = newValue;
    }

    public  int getValue(){
        return  value;
    }

    public static EnumNoteType fromInt(int newValue)
    {
        if(newValue == 1)
            return EnumNoteType.Text;
        if(newValue == 2)
            return EnumNoteType.Image;
        if(newValue == 3)
            return EnumNoteType.Video;
        if(newValue == 4)
            return EnumNoteType.GPS;
        else
            return null;
    }
}
