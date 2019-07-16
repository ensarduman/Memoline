package com.ensarduman.memoline.View.EventListeners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Runnables.DeleteNoteThread;
import com.ensarduman.memoline.View.Views.NoteContent;

import java.util.List;

/**
 * Created by duman on 25/02/2018.
 */

public class BtnEditNoteOnClickListener implements View.OnClickListener
{
    LinearLayout parentLayout;
    MainActivity mainActivity;

    public BtnEditNoteOnClickListener(MainActivity mainActivity, LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {

        //Seçilen notlar alınıyor
        final List<NoteContent> noteContents = mainActivity.getSelectedNoteContents();

        //Eğer bir adet seçili not var ise edit işlemi başlıyor
        if(noteContents.size() == 1) {
            mainActivity.startEditing(noteContents.get(0));
        }


    }

    private void StartSendDeleteNoteThread(int noteID, MainActivity mainActivity){
        DeleteNoteThread deleteNoteThread = new DeleteNoteThread(noteID, mainActivity);
        Thread thread = new Thread(deleteNoteThread);
        thread.start();
    }
}