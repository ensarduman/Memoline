package com.ensarduman.memoline.View.EventListeners;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import com.ensarduman.memoline.View.Views.HashtagEditText;

/**
 * Created by duman on 24/02/2018.
 */

public class HashtagEditTextItemClickListener implements AdapterView.OnItemClickListener {

    HashtagEditText hashtagEditText;

    public HashtagEditTextItemClickListener(HashtagEditText hashtagEditText) {
        this.hashtagEditText = hashtagEditText;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        autoComplete.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                autoComplete.showDropDown();
//            }
//        },100);
//        hashtagEditText.setText(hashtagEditText.getText().toString());
//        hashtagEditText.setSelection(hashtagEditText.getText().length());


        String val = ((AppCompatTextView) view).getText().toString();

        if(hashtagEditText.getTagType() == 1) {
            hashtagEditText.setText(val);
        }

        hashtagEditText.setSelectonToLast();
    }
}
