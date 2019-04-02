package com.example.ngoadmin;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyPostActivity extends AppCompatActivity implements PostItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private ArrayList<PostModel> postModelArrayList;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_post);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.verify_post_list);

        postModelArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(VerifyPostActivity.this, postModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManager);

        getPostData();

    }

    private void getPostData() {

        databaseReference
                .child(AppConstant.FIREBASE_POST)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postModelArrayList.clear();
                        for (final DataSnapshot postModelSnapshot : dataSnapshot.getChildren()) {
                            final PostModel postModel = postModelSnapshot.getValue(PostModel.class);
                            if (!postModel.isVerify()) {
                                databaseReference
                                        .child(AppConstant.FIREBASE_VOLUNTEER)
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                VolunteerModel volunteerModel = dataSnapshot.getValue(VolunteerModel.class);
                                                Log.e("TETST", dataSnapshot.getRef() + "");
                                                if (postModel.getArea().equals(volunteerModel.getArea())) {
                                                    databaseReference.child(AppConstant.FIREBASE_AREA)
                                                            .child(postModel.getArea())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    AreaModel areaModel = dataSnapshot.getValue(AreaModel.class);
                                                                    postModel.setArea(areaModel.getAreaName());
                                                                    postAdapter.notifyDataSetChanged();

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                    databaseReference.child(AppConstant.FIREBASE_CATEGORY)
                                                            .child(postModel.getCategory())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    CategoryModel categoryModel = dataSnapshot.getValue(CategoryModel.class);
                                                                    postModel.setCategory(categoryModel.getCategoryName());
                                                                    postAdapter.notifyDataSetChanged();

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                    databaseReference.child(AppConstant.FIREBASE_USER)
                                                            .child(postModel.getUserUuid())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                                    postModel.setUserUuid(userModel.getName());
                                                                    postAdapter.notifyDataSetChanged();

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                    postModel.setPushKey(postModelSnapshot.getKey());
                                                    postModelArrayList.add(postModel);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                            }


                        }
                        postAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onPostItemClickListener(final PostModel postModel) {

        Toast.makeText(this, "" + postModel.getPushKey(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyPostActivity.this);

        builder.setTitle("Verify POST");
        builder.setMessage("Are you want to verify this POST?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference
                        .child(AppConstant.FIREBASE_POST)
                        .child(postModel.getPushKey())
                        .child("verify")
                        .setValue(true, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(VerifyPostActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerifyPostActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
