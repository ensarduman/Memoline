package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.ensarduman.memoline.R;

import java.util.Locale;

/**
 * Created by duman on 17/03/2018.
 */

public class AppnameText extends android.support.v7.widget.AppCompatTextView {
    public AppnameText(Context context) {
        super(context);
        SetDefaults();
    }

    public AppnameText(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetDefaults();
    }

    public AppnameText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaults();
    }

    private void SetDefaults()
    {
        //@color/appnameText
        this.setTextColor(getResources().getColor(R.color.appnameText));

        //@string/app_name
        this.setText(R.string.app_name);

        //Uygulama başlığı fontu ayarlanıyor
        AssetManager am = super.getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "vytorla-mix.ttf"));
        this.setTypeface(typeface);
    }

}
