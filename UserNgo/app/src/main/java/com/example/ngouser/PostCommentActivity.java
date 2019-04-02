package com.example.ngouser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostCommentActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Intent intent;
    private TextView title;
    private TextView description;
    private TextView username;
    private TextView category;
    private TextView area;
    private TextView date;
    private ImageView img;

    private EditText commentEd;
    private Button addBtn;
    String pushKey;
    private RecyclerView recyclerView;
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();

        intent = getIntent();

        pushKey = intent.getStringExtra("KEY_PUSH");
        getPostData(pushKey);


        recyclerView = findViewById(R.id.comment_list);

        commentModelArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(PostCommentActivity.this, commentModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(PostCommentActivity.this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);


        getCommentData();

    }

    private void getCommentData() {

        databaseReference.child(AppConstant.FIREBASE_COMMENT)
                .child(pushKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        commentModelArrayList.clear();
                        for (DataSnapshot commentModelSnapshot : dataSnapshot.getChildren()) {
                            CommentModel commentModel = commentModelSnapshot.getValue(CommentModel.class);
                            commentModelArrayList.add(commentModel);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void initView() {
        title = findViewById(R.id.comment_post_title);
        description = findViewById(R.id.comment_post_description);
        username = findViewById(R.id.comment_post_user);
        category = findViewById(R.id.comment_post_category);
        area = findViewById(R.id.comment_post_area);
        date = findViewById(R.id.comment_post_date);
        img = findViewById(R.id.comment_post_img);

        commentEd = findViewById(R.id.comment_title);
        addBtn = findViewById(R.id.comment_add_btn);

        addBtn.setOnClickListener(this);

    }

    private void getPostData(String pushKey) {

        databaseReference
                .child(AppConstant.FIREBASE_POST)
                .child(pushKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final PostModel postModel = dataSnapshot.getValue(PostModel.class);


                        if (postModel.isVerify()) {
                            databaseReference.child(AppConstant.FIREBASE_AREA)
                                    .child(postModel.getArea())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            AreaModel areaModel = dataSnapshot.getValue(AreaModel.class);
                                            postModel.setArea(areaModel.getAreaName());


                                            databaseReference.child(AppConstant.FIREBASE_USER)
                                                    .child(postModel.getUserUuid())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                            postModel.setUserUuid(userModel.getName());


                                                            databaseReference.child(AppConstant.FIREBASE_CATEGORY)
                                                                    .child(postModel.getCategory())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            CategoryModel categoryModel = dataSnapshot.getValue(CategoryModel.class);
                                                                            postModel.setCategory(categoryModel.getCategoryName());

                                                                            title.setText(postModel.getTitle());
                                                                            description.setText(postModel.getDescription());
                                                                            date.setText(postModel.getDate());
                                                                            username.setText(postModel.getUserUuid());
                                                                            category.setText(postModel.getCategory());
                                                                            area.setText(postModel.getArea());
                                                                            Glide.with(PostCommentActivity.this)
                                                                                    .load(postModel.getImageUrl())
                                                                                    .placeholder(R.mipmap.ic_launcher_round)
                                                                                    .into(img);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });


                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {

        final String message = commentEd.getText().toString().trim();

        if (!message.isEmpty()) {

            databaseReference
                    .child(AppConstant.FIREBASE_COMMENT)
                    .child(pushKey)
                    .push()
                    .setValue(new CommentModel(firebaseAuth.getCurrentUser().getEmail(),
                            message), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(PostCommentActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                              commentEd.setText("");
                                Toast.makeText(PostCommentActivity.this, "Success Add", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Please fill the comment", Toast.LENGTH_SHORT).show();

        }

    }
}
