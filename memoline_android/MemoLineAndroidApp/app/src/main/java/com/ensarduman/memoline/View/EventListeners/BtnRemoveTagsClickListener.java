package com.ensarduman.memoline.View.EventListeners;

import android.view.View;

import com.ensarduman.memoline.View.MainActivity;

/**
 * Created by duman on 09/06/2018.
 */

public class BtnRemoveTagsClickListener implements View.OnClickListener {

    MainActivity mainActivity;

    public BtnRemoveTagsClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        this.mainActivity.removeTags();
    }
}
