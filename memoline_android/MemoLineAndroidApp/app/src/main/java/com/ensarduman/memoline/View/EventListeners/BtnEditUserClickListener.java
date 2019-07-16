package com.ensarduman.memoline.View.EventListeners;

import android.content.Intent;
import android.view.View;

import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.UserDetailActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnEditUserClickListener implements View.OnClickListener  {

    MainActivity mainActivity;

    public BtnEditUserClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mainActivity, UserDetailActivity.class);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }
}
