package com.ensarduman.memoline.View.Runnables;


import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.View.MainActivity;

import java.util.List;

/**
 * Created by ensarduman on 26.02.2018.
 */

//*
// Silinme bilgisi server'e henüz gönderilmemiş notların silme bilgisini gönderir
// */
public class DeleteNotesThread implements Runnable {
    MainActivity mainActivity;

    public DeleteNotesThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        try {
            NoteBusiness noteBusiness = new NoteBusiness(mainActivity);
            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(mainActivity);
            //Silinmiş ama server'a henüz iletilmemiş notlar alınıyor
            List<NoteModel> noteModels = noteBusiness.GetNotesForSendDeletedOnServer();

            NoteModel currentNote;
            for (int i = 0; i < noteModels.size(); i++) {
                currentNote = noteModels.get(i);

                if (currentNote.getUserID() != null) {
                    synchronizationBusiness.DeleteNote(currentNote.getNoteID());
                }
            }
        }finally {
        }
    }
}
