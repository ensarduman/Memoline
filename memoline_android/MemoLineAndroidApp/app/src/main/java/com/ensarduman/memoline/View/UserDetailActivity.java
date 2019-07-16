package com.ensarduman.memoline.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.EventListeners.BtnUserChangePasswordOnClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnUserLogoutOnClickListener;
import com.facebook.login.LoginManager;

public class UserDetailActivity extends ActivityBase {


    TextView txtUserDetailEmail;
    TextView txtUserDetailName;
    TextView txtUserDetailSurname;
    UserModel currentUser;
    RelativeLayout loadingPanel;

    Button btnLogout;
    Button btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(this);
        currentUser = authenticationBusiness.GetLocalCurrentUser();

        BindControls();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void BindControls(){

        loadingPanel = findViewById(R.id.loadingPanel);

        txtUserDetailEmail = findViewById(R.id.txtUserDetailEmail);
        txtUserDetailEmail.setText(currentUser.getEmail());

        txtUserDetailName = findViewById(R.id.txtUserDetailName);
        txtUserDetailName.setText(currentUser.getName());

        txtUserDetailSurname = findViewById(R.id.txtUserDetailSurname);
        txtUserDetailSurname.setText(currentUser.getSurname());

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new BtnUserLogoutOnClickListener(this));

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new BtnUserLogoutOnClickListener(this));

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new BtnUserChangePasswordOnClickListener(this));
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
                    if((boolean)msg.obj == true) {
                        LoginManager.getInstance().logOut();
                        GoToMain();
                    }
                }

                StopLoading();
            }
        }
    };

    public Handler getHandler()
    {
        return hnd;
    }

    private void GoToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        this.startActivity(intent);
        this.finish();
    }
}
