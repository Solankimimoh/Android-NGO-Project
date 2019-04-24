package com.example.ngouser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText addressEd;
    private Spinner userTypeSpinner;
    private Button signupBtn;
    private TextView oldUserTv;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private boolean isNgo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();

    }

    private void initView() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        nameEd = findViewById(R.id.activity_signup_name_ed);
        emailEd = findViewById(R.id.activity_signup_email_ed);
        passwordEd = findViewById(R.id.activity_signup_password_ed);
        mobileEd = findViewById(R.id.activity_signup_mobile_ed);
        addressEd = findViewById(R.id.activity_signup_address_ed);
        userTypeSpinner = findViewById(R.id.activity_signup_types_stock_ed);
        signupBtn = findViewById(R.id.activity_signup_signup_btn);
        oldUserTv = findViewById(R.id.activity_signup_old_user_tv);
        progressDialog = new ProgressDialog(SignupActivity.this);

        signupBtn.setOnClickListener(this);
        oldUserTv.setOnClickListener(this);
        userTypeSpinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_signup_signup_btn:
                userRegister();
                break;
            case R.id.activity_signup_old_user_tv:
                finish();
                break;
        }

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void userRegister() {

        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();
        final String name = nameEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String address = addressEd.getText().toString().trim();
        final String types = userTypeSpinner.getSelectedItem().toString().trim();

        progressDialog.setTitle("Signup user");
        progressDialog.setMessage("Creating user....");
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || mobile.isEmpty() || address.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Details not entered", Toast.LENGTH_SHORT).show();
        } else {
            if (passwordEd.getText().toString().length() < 8 || !isValidPassword(passwordEd.getText().toString())) {

                progressDialog.dismiss();
                Toast.makeText(this, "Please enter 8 char and match password policy", Toast.LENGTH_SHORT).show();
            } else {

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignupActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                            databaseReference
                                    .child(AppConstant.FIREBASE_USER)
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .setValue(new UserModel(name
                                            , email
                                            , password
                                            , mobile
                                            , address
                                            , isNgo
                                            , false), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                Toast.makeText(SignupActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                firebaseAuth.signOut();
                                                Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }


        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            isNgo = false;
        } else if (position == 1) {
            isNgo = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
