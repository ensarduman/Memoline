package com.ensarduman.memoline.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.EventListeners.BtnUserLoginOnClickListener;

public class UserLoginActivity extends ActivityBase {

    private String email;
    RelativeLayout loadingPanel;

    TextView txtUserLoginEmail;
    EditText txtUserLoginPassword;
    Button btnUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        BindControls();
    }

    private void BindControls()
    {
        //Email bilgisi okunuyor
        Bundle b = getIntent().getExtras();
        if(b != null)
            email = b.getString("email");
        else
            email = null;

        loadingPanel = findViewById(R.id.loadingPanel);

        txtUserLoginEmail = findViewById(R.id.txtUserLoginEmail);
        txtUserLoginEmail.setText(email);

        txtUserLoginPassword = findViewById(R.id.txtUserLoginPassword);

        btnUserLogin = findViewById(R.id.btnUserLogin);
        btnUserLogin.setOnClickListener(new BtnUserLoginOnClickListener(this));
    }

    private Handler hnd = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) //IsExistsUserThread ise
            {
                if(msg.obj != null)
                {
                    GoToMain();
                }
                else
                {
                    showLoginFailedMessage();
                }

                StopLoading();
            }
        }
    };

    public Handler getHandler()
    {
        return hnd;
    }

    public String getInsertedEmail(){
        return email;
    }

    public String getInsertedPassword(){
        return txtUserLoginPassword.getText().toString();
    }

    private void GoToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        this.startActivity(intent);
        this.finish();
    }

    public void showLoginFailedMessage()
    {
        ShowToastMessageLong(getString(R.string.toastmessage_login_failed));
    }

}
