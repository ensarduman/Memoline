package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.util.AttributeSet;

import com.ensarduman.memoline.R;

/**
 * Created by duman on 17/03/2018.
 */

public class FormTextView extends android.support.v7.widget.AppCompatTextView {
    public FormTextView(Context context) {
        super(context);
        SetDefaults();
    }

    public FormTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetDefaults();
    }

    public FormTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaults();
    }

    private void SetDefaults()
    {
        setBackgroundResource(R.drawable.back_form_textview);

        int color = getContext().getResources().getColor(R.color.formEditTextBack);
        setTextColor(color);
    }
}
