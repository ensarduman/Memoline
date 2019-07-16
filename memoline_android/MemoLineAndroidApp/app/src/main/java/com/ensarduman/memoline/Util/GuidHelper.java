package com.ensarduman.memoline.Util;

import java.util.UUID;

/**
 * Created by ensarduman on 10.03.2018.
 */

public class GuidHelper {

    public static String GetRandomGuid()
    {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

}
