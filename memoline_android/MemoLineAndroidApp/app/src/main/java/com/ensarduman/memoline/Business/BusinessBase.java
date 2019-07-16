package com.ensarduman.memoline.Business;

import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.Util.EnumResponseStatus;

/**
 * Created by duman on 22/03/2018.
 */

public class BusinessBase {
    public boolean isResponseSuccess(ResponseDTO responseDTO)
    {
        boolean rv = false;

        if(responseDTO != null)
        {
            if(responseDTO.getStatusCode() == EnumResponseStatus.Success.getValue())
            {
                rv = true;
            }
        }

        return  rv;
    }
}
