package com.ensarduman.memoline.View.Views;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.DateHelper;
import com.ensarduman.memoline.Util.StringHelper;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Runnables.CheckNoteThread;
import com.ensarduman.memoline.View.Runnables.SendNoteThread;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by duman on 17/02/2018.
 */

public class NoteContent extends LinearLayout {
    NoteModel note;
    TextView txtNoteText;
    TextView txtDate;
    TextView txtHashtag;
    LinearLayout noteLayout;
    LinearLayout topSpaceLayout;
    LinearLayout noteTextLayout;
    LinearLayout dateTextLayout;
    LinearLayout bottomSpaceLayout;
    ImageView checkIcon;
    ImageView cloudIcon;
    MainActivity context;
    private boolean isSelected;
    boolean syncronizing;

    public NoteModel getNote() {
        return note;
    }

    public NoteContent(MainActivity context, NoteModel noteModel) {
        super(context);
        this.note = noteModel;
        this.context = context;
        this.syncronizing = false;

        SetProperties();
        BindViews();
    }

    private void SetProperties(){
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, MATCH_PARENT));

        this.setOrientation(VERTICAL);

        this.setPadding(20,10,20,10);
    }

    /**
     * Notun içeriğini yaratır.
     * Tekrar çağırılması halinde content'i notun
     * son durumuna göre yeniden yaratır
     * */
    public void BindViews(){
        this.removeAllViews();

        //Üst boşluk layout'u yaratılıyor. Default olarak gizli
        topSpaceLayout = new LinearLayout(context);
        topSpaceLayout.setLayoutParams(new LinearLayout.LayoutParams(20,0));
        this.addView(topSpaceLayout);


        //Genel bir not layout'u yaratılıyor
        //Üst boşluk dışında herşey bunun içinde olacak
        noteLayout = new LinearLayout(context);
        noteLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        noteLayout.setOrientation(LinearLayout.VERTICAL);
        noteLayout.setGravity(Gravity.LEFT);

        //Notun içeriği yaratılıyor
        noteTextLayout = new LinearLayout(context);
        noteTextLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        noteTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        noteTextLayout.setGravity(Gravity.LEFT);

        //Notun text'i
        txtNoteText = new TextView(context);
        txtNoteText.setLayoutParams(new LayoutParams(MATCH_PARENT,WRAP_CONTENT, 1.0f ));
        txtNoteText.setText(note.getContentText());
        txtNoteText.setTextSize(13);
        txtNoteText.setGravity(Gravity.LEFT);
        noteTextLayout.addView(txtNoteText);

        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        //Notun bulutu
        cloudIcon = new ImageView(context);
        cloudIcon.setLayoutParams(new LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.MATCH_PARENT));
        cloudIcon.setImageResource(R.drawable.notecontent_cloud_default);
        cloudIcon.setVisibility(INVISIBLE);
        cloudIcon.getLayoutParams().height = dimensionInDp;
        cloudIcon.getLayoutParams().width = dimensionInDp;
        cloudIcon.requestLayout();
        noteTextLayout.addView(cloudIcon);


        //Notun check'i
        checkIcon = new ImageView(context);
        checkIcon.setLayoutParams(new LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.MATCH_PARENT));
        checkIcon.setImageResource(R.drawable.check_note_content);

        checkIcon.getLayoutParams().height = dimensionInDp;
        checkIcon.getLayoutParams().width = dimensionInDp;
        checkIcon.requestLayout();
        noteTextLayout.addView(checkIcon);


        noteLayout.addView(noteTextLayout);

        //Notun hashtag'leri yaratılıyor
        if(note.getHashtags() != null) {
            LinearLayout hashtagTextLayout = new LinearLayout(context);
            hashtagTextLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            hashtagTextLayout.setOrientation(LinearLayout.VERTICAL);
            hashtagTextLayout.setGravity(Gravity.LEFT);

            txtHashtag = new TextView(context);
            txtHashtag.setText(StringHelper.GetHashtagStringFromList(note.getHashtags(), 2));
            txtHashtag.setGravity(Gravity.LEFT);
            txtHashtag.setTextSize(9);

            hashtagTextLayout.addView(txtHashtag);
            noteLayout.addView(hashtagTextLayout);
        }

        //Notun tarihi yaratılıyor
        dateTextLayout = new LinearLayout(context);
        dateTextLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        dateTextLayout.setOrientation(LinearLayout.VERTICAL);
        dateTextLayout.setGravity(Gravity.RIGHT);

        txtDate = new TextView(context);
        txtDate.setText(DateHelper.getDateString(DateHelper.FromUTCDate(note.getNoteDate())));
        txtDate.setGravity(Gravity.RIGHT);
        txtDate.setTextSize(10);

        dateTextLayout.addView(txtDate);
        noteLayout.addView(dateTextLayout);

        this.addView(noteLayout);

        //Notun işaretlenme durumuna göre aksiyon tetikleniyor
        setIsChecked(this.note.getIsChecked());

        //Thread çalıştırılma kontrolü
        this.StartThreadsIfMustRun();
    }

    /**Gerekli görülen thread'ler çalıştırılır*/
    public void StartThreadsIfMustRun()
    {
        //Eğer mesaj sunucuya gönderilmemişse gönderme işlemi başlatılır
        if(this.isMustSendNote()) {
            StartSendNoteThread();
        }
        else{
            StartCheckNoteThreadIfMustRun();
        }
    }

    /**Gerekli ise check thread'i çalıştırılır*/
    private void StartCheckNoteThreadIfMustRun()
    {
        //Eğer son check işlemi daha önce çalıştırılmamışsa çalıştırılır
        if(this.isMustSendChecked()) {
            StartSendCheckedThread();
        }
    }

    /**Senkronizasyon ikonunu duruma göre yeniler*/
    private void renewSyncronizationDisplay()
    {
        if(this.isMustSendNote()) {
            setSyncronizing();
        }
        else if(this.isMustSendChecked()) {
            setSyncronizing();
        }
        else
        {
            setSyncronized();
        }
    }

    private void StartSendNoteThread()
    {
        SendNoteThread sendNoteThread = new SendNoteThread(context, this);
        Thread thread = new Thread(sendNoteThread);
        thread.start();
    }

    private void StartSendCheckedThread()
    {
        CheckNoteThread checkNoteThread = new CheckNoteThread(context, this);
        Thread thread = new Thread(checkNoteThread);
        thread.start();
    }

    private Handler hnd = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            renewSyncronizationDisplay();
        }
    };

    public Handler getHandler()
    {
        return hnd;
    }

    /**Notun tiklenme durumu*/
    public void setIsChecked(boolean isChecked)
    {
        if(!this.isSelected) {
            note.setIsChecked(isChecked);
            if (isChecked) {
                noteLayout.setBackgroundResource(R.drawable.back_note_content_checked);
                txtNoteText.setTextColor(ContextCompat.getColor(context, R.color.noteContentCheckedText));
                txtDate.setTextColor(ContextCompat.getColor(context, R.color.noteContentCheckedText));
                txtHashtag.setTextColor(ContextCompat.getColor(context, R.color.noteContentCheckedText));
                checkIcon.setVisibility(VISIBLE);
                cloudIcon.setImageResource(R.drawable.notecontent_cloud_checked);
            } else {
                noteLayout.setBackgroundResource(R.drawable.back_note_content);
                txtNoteText.setTextColor(ContextCompat.getColor(context, R.color.noteContentDefaultText));
                txtDate.setTextColor(ContextCompat.getColor(context, R.color.noteContentDefaultText));
                txtHashtag.setTextColor(ContextCompat.getColor(context, R.color.noteContentDefaultText));
                checkIcon.setVisibility(INVISIBLE);
                cloudIcon.setImageResource(R.drawable.notecontent_cloud_default);
            }

            //Bu işlem sonrasında ilgili thread çalıştırılır
            StartCheckNoteThreadIfMustRun();
            renewSyncronizationDisplay();
        }
    }

    /**Notun tiklenme durumu*/
    public boolean getIsChecked()
    {
        return this.note.getIsChecked();
    }

    /**Notun üstünde boşluk yaratır*/
    public void setTopFreeSpaceEnabled()
    {
        topSpaceLayout.setVisibility(VISIBLE);
    }

    /**Notun üstündeki boşluğu kaldırır*/
    public void setTopFreeSpaceDisabled()
    {
        topSpaceLayout.setVisibility(GONE);
    }

    public void toggleNoteSelection()
    {
        if(this.isSelected)
        {
            unselectNote();
        }
        else
        {
            selectNote();
        }
    }

    public void selectNote()
    {
        isSelected = true;

        //Notun görünümü seçilme arka planına dönüştürülüyor
        noteLayout.setBackgroundResource(R.drawable.back_note_content_selected);
        txtNoteText.setTextColor(ContextCompat.getColor(context, R.color.noteContentSelectedText));
        txtDate.setTextColor(ContextCompat.getColor(context, R.color.noteContentSelectedText));
        txtHashtag.setTextColor(ContextCompat.getColor(context, R.color.noteContentSelectedText));
    }

    public void unselectNote()
    {
        isSelected = false;

        //Normal haline getiriliyor
        setIsChecked(this.getIsChecked());
    }

    /**Notun seçilme durumu*/
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /**Sadece görüntü olarak notu senkronize ediliyor
     * Bulut simgesi gösterilir*/
    private void setSyncronizing(){
        this.syncronizing = true;
        cloudIcon.setVisibility(VISIBLE);
    }

    /**
     * Senkronizasyon var ise ikinci bir senkronizasyon başlamaması için bu metod geliştirildi
     * */
    public boolean isSyncronizing() {
        return this.syncronizing;
    }

    /**Sadece görüntü olarak notu senkronize ediliyor
     * Bulut simgesi gösterilir*/
    private void setSyncronized(){
        this.syncronizing = false;
        cloudIcon.setVisibility(INVISIBLE);
    }

    /**Gönderim durumunu gönderildi yapar*/
    public void setNoteSent()
    {
        //serverNoteID db'ye yazılıyor
        NoteBusiness business = new NoteBusiness(context);
        business.SetOnServer(this.note.getNoteID(), true);
        this.note.setOnServer(true);
        StartThreadsIfMustRun();
    }

    /**tiklenme durumunu gönderildi yapar*/
    public void setNoteSentChecked()
    {
        NoteBusiness business = new NoteBusiness(context);
        business.SetCheckedOnServer(this.note.getNoteID(), this.note.isChecked());
        this.note.setCheckedOnServer(this.note.isChecked());
    }

    public boolean isMustSendNote()
    {
        boolean rv = false;

        if(this.note.getUserID() != null) {
            rv = !this.note.isOnServer();
        }

        return rv;
    }

    public boolean isMustSendChecked()
    {
        boolean rv = false;

        if(this.note.getUserID() != null) {
            rv = this.note.isCheckedOnServer() != this.note.isChecked();
        }

        return rv;
    }

    /**Server'a Gönderim durumunu döner*/
    public boolean isSent()
    {
        return this.note.isOnServer();
    }
}
