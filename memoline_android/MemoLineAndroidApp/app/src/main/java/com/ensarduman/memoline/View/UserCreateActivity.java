package com.ensarduman.memoline.View;

import android.annotation.SuppressLint;
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

import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.ErrorMessageHelper;
import com.ensarduman.memoline.View.EventListeners.BtnUserCreateOnClickListener;
import com.ensarduman.memoline.View.Views.FormContinueButton;
import com.ensarduman.memoline.View.Views.FormEditText;
import com.ensarduman.memoline.View.Views.FormTextView;

public class UserCreateActivity extends ActivityBase {

    RelativeLayout loadingPanel;

    FormEditText txtUserCreateEmail;
    FormEditText txtUserCreateName;
    FormEditText txtUserCreateSurname;
    FormEditText txtUserCreatePassword;
    FormEditText txtUserCreateRePassword;
    FormContinueButton btnUserCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);

        BindControls();
    }

    private void BindControls()
    {
        String email;
        //Email bilgisi okunuyor
        Bundle b = getIntent().getExtras();
        if(b != null)
            email = b.getString("email");
        else
            email = null;

        loadingPanel = findViewById(R.id.loadingPanel);

        txtUserCreateEmail = findViewById(R.id.txtUserCreateEmail);
        txtUserCreateEmail.setText(email);

        txtUserCreateName = findViewById(R.id.txtUserCreateName);
        txtUserCreateSurname = findViewById(R.id.txtUserCreateSurname);
        txtUserCreatePassword = findViewById(R.id.txtUserCreatePassword);
        txtUserCreateRePassword = findViewById(R.id.txtUserCreateRePassword);

        btnUserCreate = findViewById(R.id.btnUserCreate);
        btnUserCreate.setOnClickListener(new BtnUserCreateOnClickListener(this));
    }

    private Handler hnd = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) //IsExistsUserThread ise
            {
                if(msg.obj != null) {
                    if ((int) msg.obj == 1) {
                        GoToMain();
                    } else if ((int) msg.obj == 2) {
                        ShowProcessError();
                    } else if ((int) msg.obj == 3) {
                        ShowEmailAlreadyExistsError();
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

    private void ShowProcessError()
    {
        ShowToastMessageLong(new ErrorMessageHelper(this).GetProcessError());
    }

    private void ShowEmailAlreadyExistsError()
    {
        ShowToastMessageLong(new ErrorMessageHelper(this).GetFieldErrorUserAlreadyExists());
    }

    public UserModel getInsertedUser() {
        UserModel rv = new UserModel();
        rv.setEmail(txtUserCreateEmail.getText().toString());
        rv.setName(txtUserCreateName.getText().toString());
        rv.setSurname(txtUserCreateSurname.getText().toString());
        return rv;
    }

    public String getInsertedPassword(){
        return txtUserCreatePassword.getText().toString();
    }

    public boolean getIsPasswordsMatch()
    {
        return (txtUserCreatePassword.getText().toString().equals(txtUserCreateRePassword.getText().toString()));
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
