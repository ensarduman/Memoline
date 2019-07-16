package com.ensarduman.memoline.View.Runnables;


import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Views.NoteContent;

/**
 * Created by ensarduman on 26.02.2018.
 */

public class SendNoteThread implements Runnable {

    NoteContent noteContent;
    MainActivity mainActivity;

    public SendNoteThread(MainActivity mainActivity, NoteContent noteContent) {
        this.noteContent = noteContent;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = 1;

        try {
            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(mainActivity);
            boolean isSent = synchronizationBusiness.SendNote(noteContent.getNote());
            if(isSent) {
                noteContent.setNoteSent();
            }
        }
        catch (Exception e)
        {
            message.obj = null;
        }

        //Bu işlem yapılırken not silinmiş olabilir
        // bu durumda content kaldırılmıştır.
        // olmayan content'e mesaj gönderilemez
        try {
            Handler handler = noteContent.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
