package com.ensarduman.memoline.View.EventListeners;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

public class BtnCopyNotesOnClickListener implements View.OnClickListener
{
    MainActivity mainActivity;

    public BtnCopyNotesOnClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {

        //Kopyalanan text
        String copiedText = "";

        //Seçilen notlar alınıyor
        final List<NoteContent> noteContents = mainActivity.getSelectedNoteContents();

        NoteContent noteContent;

        //Seçilen Notlar içinde dönülüyor
        for(int i = 0; i < noteContents.size(); i++) {
            noteContent = noteContents.get(i);

            copiedText += noteContent.getNote().getContentText();

            if(i + 1 < noteContents.size()) {
                copiedText += "\n";
            }
        }

        //Text clipboard'a ekleniyor
        ClipboardManager clipboard = (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(copiedText, copiedText);
        clipboard.setPrimaryClip(clip);

        //Kopyalandı mesajı
        mainActivity.ShowToastMessageShort(mainActivity.getString(R.string.toastmessage_notes_copied));

        //Not seçim modu kapatılıyor
        mainActivity.cancelSelectionAndEditing();
    }
}