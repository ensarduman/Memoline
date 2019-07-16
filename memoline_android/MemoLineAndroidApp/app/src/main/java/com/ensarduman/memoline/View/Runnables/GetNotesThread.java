package com.ensarduman.memoline.View.Runnables;


import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.View.MainActivity;

/**
 * Created by ensarduman on 26.02.2018.
 */

public class GetNotesThread implements Runnable {
    MainActivity mainActivity;

    public GetNotesThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = 1;

        try {
            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(mainActivity);
            message.obj = synchronizationBusiness.GetUpdates();
        }catch (Exception e){
            message.obj = null;
        }

        //Activity'nin kaldırılması durumu için try catch kullanıldı
        try {
            Handler handler = mainActivity.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
