package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;


import com.ensarduman.memoline.Business.NoteBusiness;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.Adapters.AutoCompleteAdapter;
import com.ensarduman.memoline.View.EventListeners.HashtagEditTextClickListener;
import com.ensarduman.memoline.View.EventListeners.HashtagEditTextItemClickListener;
import com.ensarduman.memoline.View.EventListeners.OnTextChangedListener;
import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.View.Tokenizers.SpaceTokenizer;
import com.ensarduman.memoline.Util.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by duman on 19/02/2018.
 */

public class HashtagEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {

    Context context;
    OnTextChangedListener textChangedListener;
    private int onScreenLocation;
    private int tagType;
    TypedArray typedArray = null;

    public HashtagEditText(Context context) {
        super(context);
        this.context = context;
        BindControl();
    }

    public HashtagEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        //locationOnScreen bilgisi xml'den okunuyor
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.HashtagEditText);
        onScreenLocation = typedArray.getInteger(R.styleable.HashtagEditText_locationOnScreen, 1);

        //multiTag bilgisi xml'den okunuyor
        tagType = typedArray.getInteger(R.styleable.HashtagEditText_tagType, 1);


        this.context = context;
        BindControl();
    }

    /*locationOnScreen bilgisi 1 ise top, 2 ise bottom**/
    public int getOnScreenLocation() {
        return this.onScreenLocation;
    }

    /*tagType bilgisi 1 ise single, 2 ise multi**/
    public int getTagType() {
        return this.tagType;
    }

    /**
     * Kontrolün temel işlevleri hazırlanır
     */
    private void BindControl() {
        LoadItems(GetLastUsedTags());

        //metin alanına her tıklandığında menünün tekrar görünmesi sağlanıyor
        setOnClickListener(new HashtagEditTextClickListener(this));

        //autocomplete'de bir item'a tıklanınca olacaklar
        setOnItemClickListener(new HashtagEditTextItemClickListener(this));
    }

    /**
     * Autocomplete için verileri yükler
     */
    private void LoadItems(List<String> values) {
        //Boşluklardan sonra da menü çıksın
        this.setTokenizer(new SpaceTokenizer());

        //Arama menüsünün background'u ayarlanıyor
        if (onScreenLocation == 1) {
            this.setDropDownBackgroundResource(R.drawable.back_hashtag_autocomplete_top);
        } else {
            this.setDropDownBackgroundResource(R.drawable.back_hashtag_autocomplete_bottom);
            Collections.reverse(values);
        }

        //Creating the instance of ArrayAdapter contaizning list of fruit names
        AutoCompleteAdapter<String> adapter = new AutoCompleteAdapter<String>
                (this.context, android.R.layout.select_dialog_item, values);

        //SearchableAdapter adapter = new SearchableAdapter(context, values);
        //Getting the instance of AutoCompleteTextView
        this.setThreshold(1);//will start working from first character
        this.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (focused) {
            if (this.getText().toString().equals(new String(""))) {
                this.Clear();
                LoadItems(GetLastUsedTags());
                performFiltering(getText(), 0);
            }
        }
    }

    /**
     * İmleçi sona alır
     */
    public void setSelectonToLast() {
        this.setSelection(this.getText().toString().length());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        //Text alanı hashtag formatına uygun hale getiriliyor
        int oldLength = text.length();
        String newText = StringHelper.GetHashtagStringFromList(this.getHashTags(), tagType);


        if (text.length() > 0) {

            String lastWord = GetLastWritingWord(text.toString(), start);

            if(this.getTagType() == 1){
                LoadItems(GetLastUsedTags());
            }
            else {
                LoadItems(SearchTags(lastWord));
            }

            performFiltering(getText(), 0);
        } else {
            //LoadItems(GetLastUsedTags());
        }

        if (newText.length() > text.length()) {
            this.setText(newText);
            this.setSelectonToLast();
        } else if (text.length() > 1) {
            //Son karakter alınıyor
            char lastChar = text.charAt(text.length() - 1);
            //Sondan bir önceki karakter alınıyor
            char lastLastChar = text.charAt(text.length() - 2);

            //tagType single ise de boşluk'a izin verilmiyor
            if (lastLastChar == ' ' || lastLastChar == '\n' || tagType == 1) {
                if (lastChar == ' ' || lastChar == '\n') {
                    //Son yazılan karakter iptal ediliyor
                    this.setText(text.toString().substring(0, text.length() - 1));
                    this.setSelectonToLast();
                }

                //tagType single ise text'deki boşluklar her şekilde kaldırılıyor
                if (tagType == 1 && text.toString().contains(" ")) {
                    this.setText(text.toString().replace(" ", ""));
                    this.setSelectonToLast();
                }
            }
        }


        //TextChangedListener varsa çalıştırılıyor
        if (textChangedListener != null) {
            textChangedListener.OnTextChanged(text, start, lengthBefore, lengthAfter);
        }
    }

    /**
     * Text alnının temizler, kare ekler, kontrolü sona alır
     */
    public void Clear() {
        this.setText("");
        this.setSelectonToLast();
    }

    /**
     * Yazılan metni hashtag listesi olarak döndürür
     */
    public List<HashtagModel> getHashTags() {
        return StringHelper.GetHashtagListFromSstring(this.getText().toString());
    }

    /**
     * Hashtag listesini yazıya çevirerek metin kutusuna yazar
     */
    public void setHashTags(List<HashtagModel> hashtags) {
        //Eğer null ise boş text, değilse hashtagText yazılıyor
        if (hashtags == null) {
            this.setText("");
        } else {
            this.setText(StringHelper.GetHashtagStringFromList(hashtags, tagType));
        }
    }

    /**
     * Text changed event'ini set eder
     */
    public void setOnTextChangedListener(OnTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    /**
     * Son yazılan kelimeyi döner
     */
    private String GetLastWritingWord(String text, int cursorPoint) {
        int spaceLastIndex = text.lastIndexOf(' ');
        int start = 0;

        if (spaceLastIndex >= 0) {
            start = spaceLastIndex;
        }

        String rv = text.substring(start);
        return rv;
    }

    /**
     * Son kullanılan tag'leri döner
     */
    private List<String> GetLastUsedTags() {
        return SearchTags("");
    }

    /**
     * Aranan tag'leri son kullanım sırasına göre döner
     */
    private List<String> SearchTags(String keyword) {
        int maxCount = 5;

        keyword = StringHelper.ClearHashtagWord(keyword.trim());

        List<String> rv = new ArrayList<>();
        List<HashtagModel> res = new ArrayList<>();

        NoteBusiness noteBusiness = new NoteBusiness(this.context);
        if (keyword.trim().length() == 0) {
            res = noteBusiness.GetLastHashTags(maxCount);

            String defaultTags[] = getResources().getStringArray(R.array.defaultTags);

            //Eğer uygulama ilk kez kullanılıyorsa default tag'ler getiriliyor
            if (res.size() < defaultTags.length) {


                for (int i = 0; i < defaultTags.length; i++) {
                    noteBusiness.CreateOrUpdateHashtag(new HashtagModel(defaultTags[i], new Date(1100 + 1)));
                }

                res = noteBusiness.GetLastHashTags(maxCount);
            }

        } else {
            res = noteBusiness.SearchHashTags(keyword, maxCount);
        }

        for (int i = 0; i < res.size(); i++) {
            rv.add("#" + res.get(i).getHashtagValue());
        }

        return rv;
    }
}
