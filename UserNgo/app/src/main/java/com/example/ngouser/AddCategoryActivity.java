package com.example.ngouser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private EditText categoryNameEd;
    private Button addBtn;
    private RecyclerView recyclerView;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();


        recyclerView = findViewById(R.id.add_category_list);

        categoryModelArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(AddCategoryActivity.this, categoryModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(layoutManager);


        getAreaList();


    }

    private void getAreaList() {

        databaseReference
                .child(AppConstant.FIREBASE_CATEGORY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryModelArrayList.clear();
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            CategoryModel categoryModel = areaSnapshot.getValue(CategoryModel.class);
                            categoryModelArrayList.add(categoryModel);
                        }
                        categoryAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initView() {
        categoryNameEd = findViewById(R.id.add_category_name);
        addBtn = findViewById(R.id.add_category_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        final String name = categoryNameEd.getText().toString().trim();


        if (!name.isEmpty()) {

            databaseReference
                    .child(AppConstant.FIREBASE_CATEGORY)
                    .push()
                    .setValue(new CategoryModel(name), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Toast.makeText(AddCategoryActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddCategoryActivity.this, "Success Add", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        } else {
            Toast.makeText(this, "Please Fill the details", Toast.LENGTH_SHORT).show();
        }


    }
}
