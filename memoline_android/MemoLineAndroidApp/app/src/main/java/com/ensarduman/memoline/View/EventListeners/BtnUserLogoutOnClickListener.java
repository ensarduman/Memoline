package com.ensarduman.memoline.View.EventListeners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.Runnables.LogoutUserThread;
import com.ensarduman.memoline.View.UserDetailActivity;

/**
 * Created by duman on 15/03/2018.
 */

public class BtnUserLogoutOnClickListener implements View.OnClickListener {
    UserDetailActivity userDetailActivity;

    public BtnUserLogoutOnClickListener(UserDetailActivity userDetailActivity) {
        this.userDetailActivity = userDetailActivity;
    }

    @Override
    public void onClick(View view) {
        final UserDetailActivity userDetailActivity_ = userDetailActivity;
        //Logout için onay mesajı için listener
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            userDetailActivity.StartLoading();
                            StartLogoutUserThread(userDetailActivity);
                        }
                        finally {
                            userDetailActivity.StopLoading();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        //Logout için onay mesajı gösteriliyor
        AlertDialog.Builder builder = new AlertDialog.Builder(userDetailActivity);
        builder.setMessage(R.string.logout_confirmation).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private void StartLogoutUserThread(UserDetailActivity userDetailActivity){
        LogoutUserThread logoutUserThread = new LogoutUserThread(userDetailActivity);
        Thread thread = new Thread(logoutUserThread);
        thread.start();
    }

}
