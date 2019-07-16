package com.ensarduman.memoline.View;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.MemolineApp;
import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.EnumNoteType;
import com.ensarduman.memoline.Util.StringHelper;
import com.ensarduman.memoline.View.EventListeners.BtnAddUserClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnCopyNotesOnClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnDeleteNoteOnClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnEditNoteOnClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnEditUserClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnRemoveFilterClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnRemoveTagsClickListener;
import com.ensarduman.memoline.View.EventListeners.NoteContentClickListener;
import com.ensarduman.memoline.View.EventListeners.NoteContentLongClickListener;
import com.ensarduman.memoline.View.EventListeners.OnTextChangedListener;
import com.ensarduman.memoline.View.EventListeners.ScrollViewOnSizeChangedListener;
import com.ensarduman.memoline.View.Runnables.DeleteNotesThread;
import com.ensarduman.memoline.View.Runnables.GetNotesThread;
import com.ensarduman.memoline.View.Views.HashtagEditText;
import com.ensarduman.memoline.View.Views.NoteContent;
import com.ensarduman.memoline.View.Views.NotesScrollView;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActivityBase {

    /** Tag for logging */
    //private final static String TAG = "mainActivity";

    ImageButton btnSave;
    LinearLayout llyNotes;
    LinearLayout llyFilter;
    NotesScrollView scvNotes;
    EditText textInput;
    HashtagEditText textHashtagInput;
    HashtagEditText textFilter;
    Boolean filterIsChecked;
    TextView textAppName;
    ImageButton btnFilterChecked;
    ImageButton btnDeleteNote;
    ImageButton btnCopyNotes;
    ImageButton btnEditNote;
    ImageButton btnReload;
    ImageButton btnAddUser;
    ImageButton btnEditUser;
    ImageButton btnRemoveFilter;
    ImageButton btnRemoveTags;
    LinearLayout llySelectedActions;
    LinearLayout llyMainActions;
    UserModel currentUser;
    Menu menu;
    NoteContent editingNoteContent = null;

    /** Application global variables */
    MemolineApp memoLine;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(this.isInSelection()) {
            this.cancelSelectionAndEditing();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Android-Iconics
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        memoLine = MemolineApp.GetInstance(getApplicationContext());
        textAppName = findViewById(R.id.textAppName);

        //Uygulama başlığı fontu ayarlanıyor
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "vytorla-mix.ttf"));
        textAppName.setTypeface(typeface);

        //Silinmiş ve server'a henüz gönderilmemiş notlar için thread çalıştırılıyor
        StartDeletedNotesSendThread();

        //Server'daki değişiklikler alınıyor
        StartGetNotesThread();

        BindControls();
        BindNotes();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.obj != null) {
                if (msg.what == 1) { //Eğer notları alma thread'i ise

                    if ((boolean) msg.obj) {
                        btnReload.setVisibility(View.VISIBLE);
                    } else {
                        btnReload.setVisibility(View.GONE);
                    }
                }
                //else if(msg.what == 2) { //User kontrol thread'i ise
                //    if((boolean)msg.obj) {
                //        btnAddUser.setVisibility(View.VISIBLE);
                //        btnEditUser.setVisibility(View.GONE);
                //    }
                //    else
                //    {
                //        btnAddUser.setVisibility(View.GONE);
                //        btnEditUser.setVisibility(View.VISIBLE);
                //    }
                //}
            }
        }
    };

    public Handler getHandler()
    {
        return this.handler;
    }

    /**View'ları bind eder*/
    private void BindControls()
    {
        //Notun yazıldığı text alanı
        textInput = findViewById(R.id.textInput);

        //Kaydetme buttonu
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNote();
            }
        });

        //Notların olduğu Layout
        llyNotes = findViewById(R.id.llyNotes);

        //Filtrenin olduğu layout
        llyFilter = findViewById(R.id.llyFilter);

        //Filtrenin yazıldığı TextView
        textFilter = findViewById(R.id.textFilter);

        //Filter metin alanının text'i değiştirilirse filtre çalıştırılıyor
        textFilter.setOnTextChangedListener(new OnTextChangedListener() {
            @Override
            public void OnTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                if (text.length() > 0) {
                    showRemoveFilterButton();
                }
                else {
                    hideRemoveFilterButton();
                }

                ApplyFilter();
            }
        });

        //Filtre metin alanında autocomplete'den bir item seçilirse klavye kapatılıyor
        textFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
            }
        });

        //Notların olduğu ScrollView
        scvNotes = findViewById(R.id.scvNotes);

        //Scroll View'ın boyutu değiştiğinde event tetiklenmesi
        //Bu klavyenin açılıp kapanması esnasında çalışması için yapıldı
        scvNotes.setSizeChangedListener(new ScrollViewOnSizeChangedListener(llyNotes));

        //Hashtag input
        textHashtagInput = findViewById(R.id.textHashtagInput);
        textHashtagInput.setOnTextChangedListener(new OnTextChangedListener() {
            @Override
            public void OnTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                if (text.length() > 0) {
                    showRemoveTagsButton();
                }
                else {
                    hideRemoveTagsButton();
                }
            }
        });

        btnFilterChecked = findViewById(R.id.btnFilterChecked);
        btnFilterChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleFilterIsChecked();
            }
        });

        //Seçilen notların layout'u
        llySelectedActions = findViewById(R.id.llySelectedActions);

        //Normaldeki layout
        llyMainActions = findViewById(R.id.llyMainActions);

        //Not silme buttonu
        btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnDeleteNote.setOnClickListener(new BtnDeleteNoteOnClickListener(this, llyNotes));

        //Not kopyalama buttonu
        btnCopyNotes = findViewById(R.id.btnCopyNotes);
        btnCopyNotes.setOnClickListener(new BtnCopyNotesOnClickListener(this));

        //Not düzenleme buttonu
        btnEditNote = findViewById(R.id.btnEditNote);
        btnEditNote.setOnClickListener(new BtnEditNoteOnClickListener(this, llyNotes));

        //Yeniden yükleme buttonu
        btnReload = findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BindNotes();
            }
        });

        //Kullanıcı ekleme buttonu
        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new BtnAddUserClickListener(this));

        //Kullanıcı düzenleme buttonu
        btnEditUser = findViewById(R.id.btnEditUser);
        btnEditUser.setOnClickListener(new BtnEditUserClickListener(this));

        //Filtre temizleme buttonu
        btnRemoveFilter = findViewById(R.id.btnRemoveFilter);
        btnRemoveFilter.setOnClickListener(new BtnRemoveFilterClickListener(this));

        //Tag temizleme buttonu
        btnRemoveTags = findViewById(R.id.btnRemoveTags);
        btnRemoveTags.setOnClickListener(new BtnRemoveTagsClickListener(this));

        /**Login olmuş bir user var mı? Varsa accesskey geçerli mi
         * Geçerli değilse lokalde de unvalid yap*/
        //AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(this);
        //authenticationBusiness.ControlValidCurrentUser();
        ControlCurrentUser();

        //En başta seçimin inaktif olması durumu kabul ediliyor
        cancelSelectionAndEditing();
    }

    private void ControlCurrentUser()
    {
        AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(this);
        UserModel currentUser = authenticationBusiness.GetLocalCurrentUser();
        if(currentUser != null)
        {
            btnAddUser.setVisibility(View.GONE);
            btnEditUser.setVisibility(View.VISIBLE);
        }else {
            btnAddUser.setVisibility(View.VISIBLE);
            btnEditUser.setVisibility(View.GONE);
        }
    }

    /**Notları bind eder*/
    private void BindNotes()
    {
        btnReload.setVisibility(View.GONE);
        //Önce ekrandan tüm notlar temizleniyor
        ClearNotes();

        //Tüm notlar alınıyor
        NoteBusiness business = new NoteBusiness(this);
        final List<NoteModel> noteModels = business.GetAll(textFilter.getHashTags(), this.filterIsChecked);

        for (int i = 0; i < noteModels.size(); i++) {
            this.AddNote(noteModels.get(i));
        }

        cancelSelectionAndEditing();
    }

    /**Ekrana yeni not ekler*/
    private void AddNote(NoteModel noteModel)
    {
        //Notun view'ı yaratılıyor
        NoteContent layoutOutput = new NoteContent(this, noteModel);

        //Daha sonra bu NoteContent'i tespit edebilmek için benzersiz bir anahtar gerekebilir
        layoutOutput.setTag(noteModel.getNoteID());

        // Yeni not eklenince scroll'un en alta gelmesi
        layoutOutput.post(new Runnable() {
            @Override
            public void run() {;
                int getPivotY = (int)llyNotes.getPivotY();
                scvNotes.scrollBy(0, getPivotY);
            }
        });

        //Note'a basıldığında çalışacak event ekleniyor
        layoutOutput.setOnClickListener(new NoteContentClickListener(this, llyNotes));

        //Note'a uzun basıldığında çalışacak event ekleniyor
        layoutOutput.setOnLongClickListener(new NoteContentLongClickListener(this, llyNotes));

        layoutOutput.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Önce ilk not değilse boşluk ekleniyor
        if(llyNotes.getChildCount() > 0)
        {
            layoutOutput.setTopFreeSpaceEnabled();
        }
        else{
            layoutOutput.setTopFreeSpaceDisabled();
        }

        // Yeni not llyNotes'a ekleniyor
        llyNotes.addView(layoutOutput);
    }

    /**Ekrandaki tüm notları siler*/
    private void ClearNotes()
    {
        llyNotes.removeAllViews();
    }

    private void SaveNote()
    {
        NoteBusiness business = new NoteBusiness(this);

        String noteText = textInput.getText().toString();
        List<HashtagModel> hashtags = textHashtagInput.getHashTags();
        boolean localIsInEditing = isInEditing();

        //Eğer not yazılmadan save'e basılmışsa kaydedilmiyor
        if(!new String("").equals(noteText.trim())) {

            /*
            * Eğer not editleniyorsa editlenen not üzerinde işlem yapılara
            * kaydedilir.
            * Eğer yeni not yazılıyorsa not yaratılarak kaydedilir.
            * */
            NoteModel noteModel;
            if(localIsInEditing)
            {
                noteModel = getEditingNoteContent().getNote();

                /*
                * Server'a gönderilebilmesi için isOnServer false
                * yapılıyor
                * */
                noteModel.setOnServer(false);
            }
            else {
                noteModel = new NoteModel();
            }

            noteModel.setNoteType(EnumNoteType.Text);
            noteModel.setContentText(noteText);
            noteModel.setHashtags(hashtags);

            /*
            * Eğer not editleniyorsa not editleme iptal edilerek giriş alanı
            * ve hashtag alanı da temizlenir. Editlemeden de çıkılır.
            * Eğer yeni not yazılıyorsa sadece giriş alanı temizlenir.
            * */
            if(localIsInEditing) {
                this.getEditingNoteContent().BindViews();
                cancelSelectionAndEditing();
            }
            else
            {
                //not giriş alanı temizleniyor
                textInput.setText("");
            }

            //Local database'e kaydediliyor.
            business.SaveNote(noteModel);

            /*
            * Eğer not editleniyorsa notun güncel olarak görünmesi için ekran yenilenir.
            * Eğer not yaratılıyorsa ilgili not ekranda değiştirilir.
            * */
            if(localIsInEditing) {
                /*
                * Zaten NoteContent'in notu düzenlendiği için burası halihazırda
                * heap-stack olayı ile kendiliğinde olabilir.
                * */
                //BindNotes();
            }
            else {
                //Not ekrana ekleniyor
                this.AddNote(noteModel);
            }
        }
    }

    /**Her çağırıldığında isChecked filtresini değiştirir
     * Sonrasında filtreyi tekrar uygular*/
    public void ToggleFilterIsChecked()
    {
        if(filterIsChecked == null)
        {
            setFilterIsChecked(false);
        }
        else if(filterIsChecked == false)
        {
            setFilterIsChecked(true);
        }
        else if(filterIsChecked == true)
        {
            setFilterIsChecked(null);
        }

        ApplyFilter();
    }

    /**IsChecked filtresini değiştirir*/
    public void setFilterIsChecked(Boolean isChecked)
    {
        filterIsChecked = isChecked;
        if(filterIsChecked == null)
        {
            btnFilterChecked.setImageResource(R.drawable.filter_check_none);
        }
        else if(filterIsChecked == true)
        {
            btnFilterChecked.setImageResource(R.drawable.filter_check_checked);
        }
        else if(filterIsChecked == false)
        {
            btnFilterChecked.setImageResource(R.drawable.filter_check_default);
        }
    }

    /**Klavyeyi kapatır */
    public void HideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textInput.getWindowToken(), 0);
    }

    /**Klavyeyi açar */
    public void ShowKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textInput, 0);
    }

    /**Girilen filtreyi uygular*/
    private void ApplyFilter()
    {
        BindNotes();
    }

    //**Seçim durumunu başlatır*/
    public void startSelection()
    {
        llySelectedActions.setVisibility(View.VISIBLE);
        llyMainActions.setVisibility(View.GONE);

        /*
        Eğer seçilen not bir tane ise düzenlenebilir
        bu yüzden düzenleme buttonu yalnızca bir tane olduğunda
        görünür yapılıyor
         */
        if(selectedNoteCount() == 1)
        {
            btnEditNote.setVisibility(View.VISIBLE);
        }
        else
        {
            btnEditNote.setVisibility(View.GONE);
        }

        /*
        * Filtre veya Tag text alanı focus durumunda ise focus temizlenir.
        * Bunun sebebi seçim esnasında bir not silme işlemi gerçekleştirilmiş olma ihtimalidir
        * */
        textFilter.clearFocus();
        textHashtagInput.clearFocus();
    }

    //**Seçim ve edit durumunu iptal eder*/
    public void cancelSelectionAndEditing()
    {
        //Seçimler kaldırılırsa var olan editleme de iptal ediliyor
        if(isInEditing())
        {
            editingNoteContent = null;
            textHashtagInput.setText("");
            textInput.setText("");
        }

        llySelectedActions.setVisibility(View.GONE);
        llyMainActions.setVisibility(View.VISIBLE);
        btnEditNote.setVisibility(View.GONE);
        this.unSelectAllNotes();
    }

    //**Tüm notların seçimini kaldırır*/
    private void unSelectAllNotes(){
        NoteContent currentContent;

        for(int i = 0; i < llyNotes.getChildCount(); i++)
        {
           currentContent = (NoteContent) llyNotes.getChildAt(i);
           if(currentContent != null)
           {
               currentContent.unselectNote();
           }
        }
    }

    //**Seçilen notları döndürür*/
    public List<NoteContent> getSelectedNoteContents(){

        List<NoteContent> rv = new ArrayList<>();

        NoteContent currentContent;

        for(int i = 0; i < llyNotes.getChildCount(); i++)
        {
            currentContent = (NoteContent) llyNotes.getChildAt(i);
            if(currentContent != null)
            {
                if(currentContent.isSelected())
                {
                    rv.add(currentContent);
                }
            }
        }

        return rv;
    }

    //**Seçilen notların sayısını döndürür*/
    public boolean isInSelection()
    {
        boolean rv = false;

        NoteContent currentContent;
        for(int i = 0; i < llyNotes.getChildCount(); i++)
        {
            currentContent = (NoteContent) llyNotes.getChildAt(i);
            if(currentContent != null)
            {
                if(currentContent.isSelected())
                {
                    rv = true;
                }
            }
        }

        return rv;
    }

    /*
    * */
    public int selectedNoteCount()
    {
        int noteCount = 0;

        NoteContent currentContent;
        for(int i = 0; i < llyNotes.getChildCount(); i++)
        {
            currentContent = (NoteContent) llyNotes.getChildAt(i);
            if(currentContent != null)
            {
                if(currentContent.isSelected())
                {
                    noteCount++;
                }
            }
        }

        return noteCount;
    }

    //*
    // Silinmiş ve server'a silinme bilgisi henüz gönderilmemiş
    // notlar için thread çalıştırılıyor
    // */
    private void StartDeletedNotesSendThread()
    {
        DeleteNotesThread deleteNotesThread = new DeleteNotesThread(this);
        Thread thread = new Thread(deleteNotesThread);
        thread.start();
    }

    //*
    // Silinmiş ve server'a silinme bilgisi henüz gönderilmemiş
    // notlar için thread çalıştırılıyor
    // */
    private void StartGetNotesThread()
    {
        GetNotesThread getNotesThread = new GetNotesThread(this);
        Thread thread = new Thread(getNotesThread);
        thread.start();
    }

    /**
     * Filtre alanını temizleme buttonunu aktif eder
     */
    public void showRemoveFilterButton()
    {
        btnRemoveFilter.setVisibility(View.VISIBLE);
    }

    /**
     * Filtre alanını temizleme buttonunu pasif eder
     */
    public void hideRemoveFilterButton()
    {
        btnRemoveFilter.setVisibility(View.GONE);
    }

    /**
     * Filtre alanını temizler
     */
    public void removeFilter()
    {
        textFilter.setText("");
        textFilter.clearFocus();
    }

    /**
     * Tag alanını temizleme buttonunu aktif eder
     */
    public void showRemoveTagsButton()
    {
        btnRemoveTags.setVisibility(View.VISIBLE);
    }

    /**
     * Tag alanını temizleme buttonunu pasif eder
     */
    public void hideRemoveTagsButton()
    {
        btnRemoveTags.setVisibility(View.GONE);
    }

    /**
     * Tag alanını temizler
     */
    public void removeTags()
    {
        textHashtagInput.setText("");
        textHashtagInput.clearFocus();
        textHashtagInput.requestFocus();
    }

    /**
     * Klavyeyi gizler
     * */
    public void hideKeyboard() {
        View view = this.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Klavyeyi gösterir
     * */
    public void showKeyboard() {
        View view = this.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    /**
     * Düzenlenmekte olan bir not varsa onu döndürür?
     * */
    public NoteContent getEditingNoteContent()
    {
        return editingNoteContent;
    }

    /**
     * Düzenlenmekte olan bir not var mı?
     * */
    public boolean isInEditing()
    {
        return editingNoteContent != null;
    }

    /**
     * Verilen notun editlenecek not olarak kabul edilerek
     * ekranın not editleme durumuna getirilmesini sağlar
     * */
    public void startEditing(NoteContent noteContent)
    {
        showKeyboard();
        editingNoteContent = noteContent;

        String hashtagString = StringHelper.GetHashtagStringFromList(noteContent.getNote().getHashtags(), 2);

        textHashtagInput.setText(hashtagString);
        textInput.setText(noteContent.getNote().getContentText());
        textInput.requestFocus();
        textInput.setSelection(textInput.getText().toString().length());
    }


}
