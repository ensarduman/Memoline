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

public class BtnDeleteNoteOnClickListener implements View.OnClickListener
{
    LinearLayout parentLayout;
    MainActivity mainActivity;

    public BtnDeleteNoteOnClickListener(MainActivity mainActivity, LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {

        //Seçilen notlar alınıyor
        final List<NoteContent> noteContents = mainActivity.getSelectedNoteContents();


        //final NoteContent noteContent = (NoteContent)view;
        //final NoteModel note = noteContent.getNote();

        //Silme için onay mesajı için listener
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        NoteContent noteContent;

                        //Seçilen Notlar içinde dönülüyor
                        for(int i = 0; i < noteContents.size(); i++) {
                            noteContent = noteContents.get(i);

                            //Not veritabanından kaldırılıyor
                            NoteBusiness business = new NoteBusiness(mainActivity);
                            business.DeleteNote(noteContent.getNote().getNoteID());

                            //Notun bir kullanıcısı varsa silme işlemi sunucuya gönderiliyor
                            if(noteContent.getNote().getUserID() != null) {
                                StartSendDeleteNoteThread(noteContent.getNote().getNoteID(), mainActivity);
                            }

                            //NoteModel listeden kaldırılıyor
                            parentLayout.removeView(noteContent);
                        }

                        mainActivity.cancelSelectionAndEditing();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        //Silme için onay mesajı gösteriliyor
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(R.string.note_delete_confirmation).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private void StartSendDeleteNoteThread(int noteID, MainActivity mainActivity){
        DeleteNoteThread deleteNoteThread = new DeleteNoteThread(noteID, mainActivity);
        Thread thread = new Thread(deleteNoteThread);
        thread.start();
    }
}