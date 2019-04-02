package com.example.ngoadmin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddAreaActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private EditText areaEd;
    private Button addBtn;
    private RecyclerView recyclerView;
    private ArrayList<AreaModel> areaModelArrayList;
    private AreaAdapter areaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();


        recyclerView = findViewById(R.id.add_area_list);

        areaModelArrayList = new ArrayList<>();
        areaAdapter = new AreaAdapter(AddAreaActivity.this, areaModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(areaAdapter);
        recyclerView.setLayoutManager(layoutManager);


        getAreaList();


    }

    private void getAreaList() {

        databaseReference
                .child(AppConstant.FIREBASE_AREA)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        areaModelArrayList.clear();
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            AreaModel areaModel = areaSnapshot.getValue(AreaModel.class);
                            areaModelArrayList.add(areaModel);
                        }
                        areaAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initView() {
        areaEd = findViewById(R.id.add_area_name);
        addBtn = findViewById(R.id.add_area_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        final String name = areaEd.getText().toString().trim();


        if (!name.isEmpty()) {

            databaseReference
                    .child(AppConstant.FIREBASE_AREA)
                    .push()
                    .setValue(new AreaModel(name), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Toast.makeText(AddAreaActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddAreaActivity.this, "Success Add", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        } else {
            Toast.makeText(this, "Please Fill the details", Toast.LENGTH_SHORT).show();
        }


    }
}
