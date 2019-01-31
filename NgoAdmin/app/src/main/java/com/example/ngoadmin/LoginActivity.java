package com.example.ngoadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = LoginActivity.class.getSimpleName();

    private EditText emailEd;
    private EditText pwdEd;
    private RadioGroup loginType;
    private Button loginBtn;
    private TextView gotoForgotoPwd;

    //    Firebase
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        initView();
    }


    private void initView() {

        //Componenet Initlization
        emailEd = findViewById(R.id.activity_login_email_ed);
        pwdEd = findViewById(R.id.activity_login_password_ed);
        loginType = findViewById(R.id.activity_login_type_rg);
        loginBtn = findViewById(R.id.activity_login_login_btn);
        gotoForgotoPwd = findViewById(R.id.activity_login_forgot_pwd_txt);

        //ProgressDialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);


        //Event Listener
        loginBtn.setOnClickListener(this);
        gotoForgotoPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_login_btn:

                RadioButton radioButton = findViewById(loginType.getCheckedRadioButtonId());
                checkLogin(radioButton.getText().toString());
                break;

            case R.id.activity_login_forgot_pwd_txt:
                Intent gotoForgotPwd = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(gotoForgotPwd);
        }
    }

    private void checkLogin(String loginType) {

        final String email = emailEd.getText().toString();
        final String password = pwdEd.getText().toString();


        progressDialog.setTitle("Login");
        progressDialog.setMessage("Checking credentials..");
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter the details", Toast.LENGTH_SHORT).show();
        } else {
            if (loginType.equals(getString(R.string.admin))) {
                if (email.equals(AppConstant.ADMIN_USERNAME) && password.equals(AppConstant.ADMIN_PASSWORD)) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Succcess", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();
                }

            } else if (loginType.equals(getString(R.string.volunteer))) {

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    final Intent gotoHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(gotoHomeActivity);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}

