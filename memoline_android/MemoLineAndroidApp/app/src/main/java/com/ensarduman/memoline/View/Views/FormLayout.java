package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ensarduman.memoline.R;

/**
 * Created by duman on 17/03/2018.
 */

public class FormLayout extends LinearLayout {
    public FormLayout(Context context) {
        super(context);
        SetDefaults();
    }

    public FormLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SetDefaults();
    }

    public FormLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaults();
    }

    private void SetDefaults()
    {
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.back_form);
    }



}
