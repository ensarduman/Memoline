package com.ensarduman.memoline.View.EventListeners;

/**
 * Created by duman on 25/02/2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Views.NoteContent;

/**
 * Nota uzun basıldığında çalışacak event
 */
public class NoteContentLongClickListener implements View.OnLongClickListener
{
    LinearLayout parentLayout;
    MainActivity mainActivity;

    public NoteContentLongClickListener(MainActivity mainActivity, LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onLongClick(View view) {
        final NoteContent noteContent = (NoteContent)view;
        final NoteModel note = noteContent.getNote();

        //seçimin görüntüsü değiştiriliyor
        noteContent.toggleNoteSelection();

        //Eğer hiç seçili not kalmamışsa seçim durumundan çıkılıyor
        if(mainActivity != null) {
            if(mainActivity.isInSelection()) {
                mainActivity.startSelection();
            }
            else
            {
                mainActivity.cancelSelectionAndEditing();
            }
        }
        return true;
    }
}