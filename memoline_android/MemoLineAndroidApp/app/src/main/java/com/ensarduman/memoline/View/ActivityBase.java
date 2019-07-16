package com.ensarduman.memoline.View;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ensarduman.memoline.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by ensarduman on 18.03.2018.
 */

public class ActivityBase extends AppCompatActivity {
    public void ShowToastMessageShort(String message)
    {
        Toast.makeText(this , message,
                Toast.LENGTH_SHORT).show();
    }

    public void ShowToastMessageLong(String message)
    {
        Toast.makeText(this , message,
                Toast.LENGTH_LONG).show();
    }

    public void ShowToastMessagesShort(List<String> messages)
    {
        ShowToastMessageShort(GetMessageFromMessages(messages));
    }

    public void ShowToastMessagesLong(List<String> messages)
    {
        ShowToastMessageLong(GetMessageFromMessages(messages));
    }

    private String GetMessageFromMessages(List<String> messages)
    {
        String message = "";

        for(int i = 0; i < messages.size(); i++)
        {
            message += messages.get(i);

            if(messages.size() != (i + 1)) {
                message += "\n";
            }
        }

        return message;
    }

    public void StartLoading()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        CloseKeyboard();

        RelativeLayout loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
    }

    public void StopLoading()
    {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RelativeLayout loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
    }

    public void CloseKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
