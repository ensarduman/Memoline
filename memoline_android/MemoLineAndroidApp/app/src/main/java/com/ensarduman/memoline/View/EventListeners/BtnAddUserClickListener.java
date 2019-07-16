package com.ensarduman.memoline.View.EventListeners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.UserEmailActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnAddUserClickListener implements View.OnClickListener {

    MainActivity mainActivity;

    public BtnAddUserClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.mainActivity, UserEmailActivity.class);
        Bundle b = new Bundle();
        b.putInt("key", 1); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        mainActivity.startActivity(intent);
        //startActivity(intent);
        mainActivity.finish();

        //Eklenen değeri diğer activity'de okumak için örnek kod
        //Bundle b = getIntent().getExtras();
        //int value = -1; // or other values
        //if(b != null)
        //    value = b.getInt("key");
    }
}
