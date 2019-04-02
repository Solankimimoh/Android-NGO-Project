package com.example.ngouser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private static final int PICK_IMG_CODE = 100;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ArrayList<AreaModel> areaModelArrayList;
    private ArrayList<String> areaStringArrayList;
    private ArrayAdapter<String> areaAdapter;

    private ArrayList<CategoryModel> categoryModelArrayList;
    private ArrayList<String> categoryStringArrayList;
    private ArrayAdapter<String> categoryAdapter;

    private EditText title;
    private EditText description;
    private Spinner areaSpinner;
    private Spinner categorySpinner;
    private Button chooseImgBtn;
    private Button addBtn;
    private Uri selectedFileIntent;

    private String categoryPushkey;
    private String areaPushkey;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(AddPostActivity.this);

        initView();
        areaModelArrayList = new ArrayList<>();
        areaStringArrayList = new ArrayList<>();

        areaAdapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_list_item_1, areaStringArrayList);
        areaSpinner.setAdapter(areaAdapter);
        getAreaList();

        categoryModelArrayList = new ArrayList<>();
        categoryStringArrayList = new ArrayList<>();

        categoryAdapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_list_item_1, categoryStringArrayList);
        categorySpinner.setAdapter(categoryAdapter);
        getCategoryList();


    }

    private void initView() {

        title = findViewById(R.id.add_post_title);
        description = findViewById(R.id.add_post_description);
        areaSpinner = findViewById(R.id.add_post_area);
        categorySpinner = findViewById(R.id.add_post_category);
        chooseImgBtn = findViewById(R.id.add_post_choose_img);
        addBtn = findViewById(R.id.add_post_add);


        areaSpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);

        chooseImgBtn.setOnClickListener(this);
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

    private void getCategoryList() {

        databaseReference
                .child(AppConstant.FIREBASE_CATEGORY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryStringArrayList.clear();
                        categoryModelArrayList.clear();

                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            CategoryModel categoryModel = areaSnapshot.getValue(CategoryModel.class);
                            categoryModel.setCategoryPushKey(areaSnapshot.getKey());
                            categoryStringArrayList.add(categoryModel.getCategoryName());

                            categoryModelArrayList.add(categoryModel);
                        }
                        categoryAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.add_post_area:
                areaPushkey = areaModelArrayList.get(position).getAreaPushKey();
                break;
            case R.id.add_post_category:
                categoryPushkey = categoryModelArrayList.get(position).getCategoryPushKey();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_post_choose_img:
                chooseFile();
                break;
            case R.id.add_post_add:
                uploadData();
                break;
        }
    }

    private void uploadData() {

        progressDialog.setTitle("Post Add");
        progressDialog.setMessage("post adding..");
        progressDialog.show();

        final String titleStr = title.getText().toString().trim();
        final String descriptionStr = description.getText().toString().trim();

        if (selectedFileIntent != null || titleStr.isEmpty() || descriptionStr.isEmpty()) {
            final StorageReference sRef = storageReference.child("post/" + UUID.randomUUID());

            sRef.putFile(selectedFileIntent)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    insertInToDatabase(titleStr, descriptionStr, uri.toString());
                                }
                            });
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please fill the alll details", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertInToDatabase(String titleStr, String descriptionStr, String uri) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        String uuid = firebaseAuth.getCurrentUser().getUid();

        databaseReference
                .child(AppConstant.FIREBASE_POST)
                .push()
                .setValue(new PostModel(titleStr
                        , descriptionStr
                        , formattedDate, categoryPushkey, areaPushkey, uuid, uri, false), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPostActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AddPostActivity.this, "Success Add", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

    }

    private void chooseFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_IMG_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                File file = new File(data.getData().getPathSegments().toString());
                selectedFileIntent = data.getData();

                chooseImgBtn.setText("Image Selected");
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
