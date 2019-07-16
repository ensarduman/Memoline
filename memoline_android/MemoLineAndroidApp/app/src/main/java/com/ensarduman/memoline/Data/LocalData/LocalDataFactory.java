package com.ensarduman.memoline.Data.LocalData;

import android.content.Context;

import com.ensarduman.memoline.Util.EnumLocalDataType;

/**
 * Created by duman on 12/02/2018.
 */

public class LocalDataFactory{

    Context context;

    public LocalDataFactory(Context context) {
        this.context = context;
    }

    public ILocalData GetDao(EnumLocalDataType dataType)
    {
        if(dataType == EnumLocalDataType.SQLite)
        {
            return new SQLiteDao(this.context);
        }
        else {
            return null;
        }
    }
}
