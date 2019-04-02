package com.example.ngoadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddVolunteerActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Spinner areaListSpinner;

    private ArrayList<AreaModel> areaModelArrayList;
    private ArrayList<String> areaStringArrayList;
    private ArrayAdapter<String> areaAdapter;
    private EditText name;
    private EditText mobile;
    private EditText email;
    private EditText passwpord;
    private EditText address;
    private Button addBtn;
    private String areaSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_volunteer);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();

        areaListSpinner = findViewById(R.id.add_volunteer_area_list);

        areaModelArrayList = new ArrayList<>();
        areaStringArrayList = new ArrayList<>();

        areaAdapter = new ArrayAdapter<>(AddVolunteerActivity.this, android.R.layout.simple_list_item_1, areaStringArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        areaListSpinner.setAdapter(areaAdapter);

        getAreaList();
    }

    private void initView() {

        name = findViewById(R.id.add_volunteer_name);
        mobile = findViewById(R.id.add_volunteer_mobile);
        email = findViewById(R.id.add_volunteer_email);
        passwpord = findViewById(R.id.add_volunteer_pwd);
        address = findViewById(R.id.add_volunteer_address);

        addBtn = findViewById(R.id.add_volunteer_btn);

        addBtn.setOnClickListener(this);

    }

    private void getAreaList() {

        databaseReference
                .child(AppConstant.FIREBASE_AREA)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        areaStringArrayList.clear();
                        areaModelArrayList.clear();

                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            AreaModel areaModel = areaSnapshot.getValue(AreaModel.class);
                            areaModel.setAreaPushKey(areaSnapshot.getKey());
                            areaStringArrayList.add(areaModel.getAreaName());
                            areaModelArrayList.add(areaModel);
                        }
                        areaAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {


        final String emailstr = email.getText().toString().trim();
        final String mobilestr = mobile.getText().toString().trim();
        final String namestr = name.getText().toString().trim();
        final String passwordstr = passwpord.getText().toString().trim();
        final String addressstr = address.getText().toString().trim();
    }
}
