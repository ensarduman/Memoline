package com.ensarduman.memoline.View.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.ensarduman.memoline.View.EventListeners.OnSizeChangedListener;

/**
 * Created by duman on 17/02/2018.
 *
 * Bu sınıf ScrollView'ın size'ı değiştiğinde bir event tetiklenebilmesi için
 * onSizeChanged metodunu overload edip, oluşturduğum OnSizeChangedListener interface'i
 * üzerinden event listener'ı kurguladım.
 */

public class NotesScrollView extends ScrollView {

    OnSizeChangedListener sizeChangedListener;

    public void setSizeChangedListener(OnSizeChangedListener sizeChangedListener) {
        this.sizeChangedListener = sizeChangedListener;
    }

    public NotesScrollView(Context context) {
        super(context);
    }

    public NotesScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotesScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Eğer eventlistener atanmış ise event tetiklenecek
        if(sizeChangedListener != null)
        {
            sizeChangedListener.OnSizeChanged(this);
        }
    }
}
