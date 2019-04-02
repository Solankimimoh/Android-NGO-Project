package com.example.ngoadmin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class AddVolunteerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Spinner areaListSpinner;

    private ArrayList<AreaModel> areaModelArrayList;
    private ArrayList<String> areaStringArrayList;
    private ArrayAdapter<String> areaAdapter;

    private RecyclerView recyclerView;
    private ArrayList<VolunteerModel> volunteerModelArrayList;
    private VolunteerAdapter volunteerAdapter;

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


        areaModelArrayList = new ArrayList<>();
        areaStringArrayList = new ArrayList<>();

        areaAdapter = new ArrayAdapter<>(AddVolunteerActivity.this, android.R.layout.simple_list_item_1, areaStringArrayList);
        areaListSpinner.setAdapter(areaAdapter);


        recyclerView = findViewById(R.id.add_volunteer_list);

        volunteerModelArrayList = new ArrayList<>();
        volunteerAdapter = new VolunteerAdapter(AddVolunteerActivity.this, volunteerModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(volunteerAdapter);
        recyclerView.setLayoutManager(layoutManager);

        getAreaList();

        getVolunteerList();
    }

    private void getVolunteerList() {

        databaseReference
                .child(AppConstant.FIREBASE_VOLUNTEER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        volunteerModelArrayList.clear();
                        for (DataSnapshot volunteerModels : dataSnapshot.getChildren()) {
                            VolunteerModel volunteerModel = volunteerModels.getValue(VolunteerModel.class);
                            volunteerModelArrayList.add(volunteerModel);
                        }
                        volunteerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void initView() {

        name = findViewById(R.id.add_volunteer_name);
        mobile = findViewById(R.id.add_volunteer_mobile);
        email = findViewById(R.id.add_volunteer_email);
        passwpord = findViewById(R.id.add_volunteer_pwd);
        address = findViewById(R.id.add_volunteer_address);
        areaListSpinner = findViewById(R.id.add_volunteer_area_list);

        addBtn = findViewById(R.id.add_volunteer_btn);

        addBtn.setOnClickListener(this);
        areaListSpinner.setOnItemSelectedListener(this);
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

        if (!namestr.isEmpty() || !emailstr.isEmpty() || !mobilestr.isEmpty()
                || !passwordstr.isEmpty() || !addressstr.isEmpty()) {

            firebaseAuth.createUserWithEmailAndPassword(emailstr, passwordstr)
                    .addOnCompleteListener(AddVolunteerActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                databaseReference
                                        .child(AppConstant.FIREBASE_VOLUNTEER)
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(new VolunteerModel(namestr, emailstr, passwordstr, mobilestr, addressstr), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Toast.makeText(AddVolunteerActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    firebaseAuth.signOut();
                                                    Toast.makeText(AddVolunteerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(AddVolunteerActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        areaSelect = areaModelArrayList.get(position).getAreaPushKey();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
