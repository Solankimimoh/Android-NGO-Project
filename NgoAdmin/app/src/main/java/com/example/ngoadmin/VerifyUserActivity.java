package com.example.ngoadmin;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyUserActivity extends AppCompatActivity implements UserItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    private RecyclerView recyclerView;
    private ArrayList<UserModel> userModelArrayList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView = findViewById(R.id.add_volunteer_list);
        userModelArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(VerifyUserActivity.this, userModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(layoutManager);


        getUserData();

    }

    private void getUserData() {

        databaseReference
                .child(AppConstant.FIREBASE_USER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModelArrayList.clear();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UserModel userModel = userSnapshot.getValue(UserModel.class);
                            userModel.setUuid(userSnapshot.getKey());
                            if (!userModel.isVerify()) {
                                userModelArrayList.add(userModel);
                            }
                        }
                        userAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onUserItemClick(final UserModel userModel) {


        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyUserActivity.this);

        builder.setTitle("Verify USER");
        builder.setMessage("Are you want to verify this account?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference
                        .child(AppConstant.FIREBASE_USER)
                        .child(userModel.getUuid())
                        .child("verify")
                        .setValue(true, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(VerifyUserActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerifyUserActivity.this, "Success Add", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();


    }
}
