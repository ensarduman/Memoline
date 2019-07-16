package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ensarduman.memoline.R;

import java.util.zip.Inflater;

/**
 * Created by duman on 21/03/2018.
 */

public class MasterLayout extends RelativeLayout {
    RelativeLayout progressLayout;
    Context context;

    public MasterLayout(Context context) {
        super(context);
        this.context = context;
        SetDefaults();
    }

    public MasterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SetDefaults();
    }

    public MasterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        SetDefaults();
    }

    private void SetDefaults()
    {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressLayout = (RelativeLayout)layoutInflater.inflate(R.layout.progress_layout, null);
        this.addView(progressLayout);

        progressLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        progressLayout.setVisibility(GONE);
    }
}
