package com.ensarduman.memoline.View.Runnables;


import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.View.MainActivity;

/**
 * Created by ensarduman on 26.02.2018.
 */

//*
// Silinme bilgisini server'a gönderir
// */
public class DeleteNoteThread implements Runnable {

    int noteID;
    MainActivity mainActivity;

    public DeleteNoteThread(int noteID, MainActivity mainActivity) {
        this.noteID = noteID;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        try {
            NoteBusiness business = new NoteBusiness(this.mainActivity);
            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(mainActivity);
            synchronizationBusiness.DeleteNote(this.noteID);
        }
        catch (Exception e)
        {

        }
        finally {
        }
    }
}
