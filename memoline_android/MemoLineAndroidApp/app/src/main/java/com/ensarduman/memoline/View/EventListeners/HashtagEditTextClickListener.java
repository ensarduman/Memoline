package com.ensarduman.memoline.View.EventListeners;

import android.os.Handler;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;

import com.ensarduman.memoline.View.Views.HashtagEditText;

/**
 * Created by duman on 25/02/2018.
 */

public class HashtagEditTextClickListener implements View.OnClickListener {

    HashtagEditText hashtagEditText;

    public HashtagEditTextClickListener(HashtagEditText hashtagEditText) {
        this.hashtagEditText = hashtagEditText;
    }

    @Override
    public void onClick(View view) {
        hashtagEditText.showDropDown();
    }
}
