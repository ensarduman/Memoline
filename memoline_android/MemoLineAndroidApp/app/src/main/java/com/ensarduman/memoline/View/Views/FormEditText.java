package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.util.AttributeSet;

import com.ensarduman.memoline.R;

/**
 * Created by duman on 17/03/2018.
 */

public class FormEditText extends android.support.v7.widget.AppCompatEditText {
    public FormEditText(Context context) {
        super(context);
        SetDefaults();
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetDefaults();
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaults();
    }

    private void SetDefaults()
    {
        setBackgroundResource(R.drawable.back_form_edittext);
    }

    /**
     * İmleçi sona alır
     */
    public void setSelectonToLast() {
        this.setSelection(this.getText().toString().length());
    }
}
