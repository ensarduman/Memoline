package com.ensarduman.memoline.View.EventListeners;

/**
 * Created by duman on 25/02/2018.
 */

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * ScrollView'ın boyutu değiştiğinde yani klavye açılıp kapatıldığında
 * Scroll en aşağı tekrar alınması için bu listener geliştirldi
 */
public class ScrollViewOnSizeChangedListener implements OnSizeChangedListener
{

    private LinearLayout parentLayout;

    public ScrollViewOnSizeChangedListener(LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
    }

    @Override
    public void OnSizeChanged(View view) {
        int getPivotY = (int)parentLayout.getPivotY();
        ((ScrollView)view).scrollBy(0, getPivotY);
    }
}
