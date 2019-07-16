package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.util.AttributeSet;

import com.ensarduman.memoline.R;

/**
 * Created by duman on 17/03/2018.
 */

public class FormContinueButton extends android.support.v7.widget.AppCompatButton {
    public FormContinueButton(Context context) {
        super(context);
        SetDefaults();
    }

    public FormContinueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetDefaults();
    }

    public FormContinueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaults();
    }

    private void SetDefaults()
    {
        setBackgroundResource(R.drawable.back_form_continuebutton);

        int color = getContext().getResources().getColor(R.color.formContinueButton);
        setTextColor(color);
    }
}
