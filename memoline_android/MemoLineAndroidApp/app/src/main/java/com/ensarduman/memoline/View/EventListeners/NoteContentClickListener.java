package com.ensarduman.memoline.View.EventListeners;

/**
 * Created by duman on 25/02/2018.
 */


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Views.NoteContent;

/**
 * Nota basıldığında çalışacak event
 */
public class NoteContentClickListener implements View.OnClickListener
{
    LinearLayout parentLayout;
    MainActivity mainActivity;

    public NoteContentClickListener(MainActivity mainActivity, LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        NoteContent noteContent = (NoteContent)view;
        NoteModel note = noteContent.getNote();
        boolean newStatus = !noteContent.getIsChecked();

        //MainActivity seçim durumunda mı? eğer öyleyse seçimin değiştirilmesi sağlanıyor
        if(this.mainActivity != null && this.mainActivity.isInSelection())
        {
            noteContent.toggleNoteSelection();

            //Eğer hiç seçili not kalmamışsa seçim durumundan çıkılıyor
            //Tersi durumda seçim başlatılıyor
            if(mainActivity.isInSelection()) {
                /*
                 * Eğer edit durumunda başka bir nota dokunulduysa
                 * veya bulunan notun seçimi kaldırıldıysa
                 * edit işlemi iptal edilir bu yüzden burada edit işleminin
                 * iptali her şekilde yapılıyor. Çünkü eğer edit devam ediyor ise buraya
                 * hiç girilmemiş olmalı
                 * */
                if(mainActivity.isInEditing())
                {
                    mainActivity.cancelSelectionAndEditing();
                }
                else {
                    mainActivity.startSelection();
                }
            }
            else {
                mainActivity.cancelSelectionAndEditing();
            }
        }
        else {
            //Notun işaretleme durumu veritabanına kaydediliyor
            NoteBusiness business = new NoteBusiness(mainActivity);
            if (newStatus) {
                business.CheckNote(note.getNoteID());
            } else {
                business.UnCheckNote(note.getNoteID());
            }

            //NoteContent'in seçilme durumu değiştiriliyor
            noteContent.setIsChecked(newStatus);
        }
    }
}
