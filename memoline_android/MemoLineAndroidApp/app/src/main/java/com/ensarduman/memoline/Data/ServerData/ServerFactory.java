package com.ensarduman.memoline.Data.ServerData;

import android.content.Context;

import com.ensarduman.memoline.Data.ServerData.Interfaces.IAuthenticationServer;
import com.ensarduman.memoline.Data.ServerData.Interfaces.INoteServer;
import com.ensarduman.memoline.Data.ServerData.WebApi.WebApiAuthenticationServer;
import com.ensarduman.memoline.Data.ServerData.WebApi.WebApiNoteServer;
import com.ensarduman.memoline.Util.EnumServerDataType;

/**
 * Created by duman on 08/03/2018.
 */

public class ServerFactory {
    Context context;

    public ServerFactory(Context context) {
        this.context = context;
    }

    public IAuthenticationServer GetAuthenticationServer(EnumServerDataType dataType)
    {
        if(dataType == EnumServerDataType.WebApi)
        {
            return new WebApiAuthenticationServer(this.context);
        }
        else {
            return null;
        }
    }

    public IAuthenticationServer GetAuthenticationServer(EnumServerDataType dataType, String accessKey)
    {
        if(dataType == EnumServerDataType.WebApi)
        {
            return new WebApiAuthenticationServer(this.context, accessKey);
        }
        else {
            return null;
        }
    }

    public INoteServer GetNoteServer(EnumServerDataType dataType, String accessKey)
    {
        if(dataType == EnumServerDataType.WebApi)
        {
            return new WebApiNoteServer(this.context, accessKey);
        }
        else {
            return null;
        }
    }
}
