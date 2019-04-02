package com.example.ngouser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEd;
    private EditText passwordEd;
    private Button loginbtn;
    private TextView forgotpwdTv;
    private TextView newuserTv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        if (firebaseAuth.getCurrentUser() != null) {
            final Intent gotoHome = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(gotoHome);
            finish();
        }

    }

    private void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        emailEd = findViewById(R.id.activity_login_email_ed);
        passwordEd = findViewById(R.id.activity_login_password_ed);
        loginbtn = findViewById(R.id.activity_login_btn);
        forgotpwdTv = findViewById(R.id.activity_login_forgot_pwd_tv);
        newuserTv = findViewById(R.id.activity_login_new_user_tv);
        progressDialog = new ProgressDialog(LoginActivity.this);


        loginbtn.setOnClickListener(this);
        forgotpwdTv.setOnClickListener(this);
        newuserTv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_login_btn:
                userLogin();
                break;
            case R.id.activity_login_forgot_pwd_tv:
                forgotPassword();
                break;
            case R.id.activity_login_new_user_tv:
                newUser();
                break;
        }

    }

    private void userLogin() {
        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();

        progressDialog.setTitle("Login user");
        progressDialog.setMessage("checking auth....");
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Enter details", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            } else {

                                databaseReference
                                        .child(AppConstant.FIREBASE_USER)
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                if (!userModel.isVerify()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "Not Verified yet", Toast.LENGTH_SHORT).show();
                                                    firebaseAuth.signOut();
                                                } else {
                                                    progressDialog.dismiss();
                                                    final Intent verifyUser = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(verifyUser);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                    });
        }
    }


    private void forgotPassword() {
        final Intent gotoForgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(gotoForgotPassword);
    }

    private void newUser() {
        final Intent gotoNewUser = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(gotoNewUser);
    }


}
