package com.ensarduman.memoline.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.EnumLoginType;
import com.ensarduman.memoline.View.EventListeners.BtnFacebookLoginClick;
import com.ensarduman.memoline.View.EventListeners.BtnUserEmailOnClickListener;
import com.ensarduman.memoline.View.Views.FormContinueButton;
import com.ensarduman.memoline.View.Views.FormEditText;
import com.ensarduman.memoline.View.Views.FormNewButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class UserEmailActivity extends ActivityBase {

    FormEditText txtUserEmail;
    FormContinueButton btnUserEmail;
    FormNewButton btnUserNew;
    RelativeLayout loadingPanel;
    CallbackManager callbackManager;
    LoginButton loginButton;
    private final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_email);
        BindControls();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void BindControls()
    {
        //Facebook login
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().registerCallback(callbackManager, new BtnFacebookLoginClick(this));
        //Facebook login



        txtUserEmail = findViewById(R.id.txtUserEmail);

        btnUserEmail = findViewById(R.id.btnUserEmail);
        btnUserEmail.setOnClickListener(new BtnUserEmailOnClickListener(this, EnumLoginType.Basic.getValue()));

        btnUserNew = findViewById(R.id.btnUserNew);
        btnUserNew.setOnClickListener(new BtnUserEmailOnClickListener(this, 0));

        loadingPanel = findViewById(R.id.loadingPanel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler hnd = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.obj != null) {
                if (msg.what == EnumLoginType.Basic.getValue()) //Giriş ise
                {
                    if ((boolean) msg.obj == true) //Kullanıcı var mı?
                    {
                        //Var ise
                        GoToUserLogin();
                    } else {
                        //Yok ise
                        ShowEmailWrongMessage();
                    }
                }
                else if (msg.what == EnumLoginType.Facebook.getValue()) //Facebook login ise
                {
                    if ((boolean) msg.obj == true)
                    {
                        //Facebook login başarılı
                        GoToMain();
                    } else {
                        //Facebook login başarısız
                        LoginManager.getInstance().logOut();
                        ShowFacebookLoginFailedMessage();
                    }
                }
                else if (msg.what == 0) //Yeni kullanıcı ise
                {
                    if ((boolean) msg.obj == false) //Kullanıcı yok mu
                    {
                        //Yok ise
                        GoToUserCreate();
                    } else {
                        //Var ise
                        ShowEmailAlreadyExistsMessage();
                    }
                }
            }

            StopLoading();
        }
    };

    public Handler getHandler()
    {
        return hnd;
    }

    /*
    * Email alanına girilen email'i döner
    * */
    public String getInsertedEmail()
    {
        return txtUserEmail.getText().toString();
    }

    private void GoToUserLogin()
    {
        Intent intent = new Intent(this, UserLoginActivity.class);
        Bundle b = new Bundle();
        b.putString("email", this.getInsertedEmail());
        intent.putExtras(b);
        this.startActivity(intent);
    }

    private void GoToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        this.startActivity(intent);
        this.finish();
    }

    private void GoToUserCreate()
    {
        Intent intent = new Intent(this, UserCreateActivity.class);
        Bundle b = new Bundle();
        b.putString("email", this.getInsertedEmail());
        intent.putExtras(b);
        this.startActivity(intent);
    }

    boolean isEmailValid() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(txtUserEmail.getText()).matches();
    }

    private void ShowEmailWrongMessage(){
        ShowToastMessageLong(getString(R.string.toastmessage_email_wrong));
    }

    private void ShowFacebookLoginFailedMessage(){
        ShowToastMessageLong(getString(R.string.toastmessage_facebook_login_failed));
    }

    private void ShowEmailAlreadyExistsMessage(){
        ShowToastMessageLong(getString(R.string.toastmessage_user_already_exists));
    }
}
