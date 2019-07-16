package com.ensarduman.memoline.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.View.EventListeners.BtnUserChangePasswordOnClickListener;
import com.ensarduman.memoline.View.EventListeners.BtnUserLogoutOnClickListener;

public class UserChangePassword extends AppCompatActivity {
    RelativeLayout loadingPanel;
    UserModel currentUser;
    TextView txtUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(this);
        currentUser = authenticationBusiness.GetLocalCurrentUser();

        BindControls();
    }

    private void BindControls(){
        loadingPanel = findViewById(R.id.loadingPanel);

        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserEmail.setText(currentUser.getEmail());
    }
}
