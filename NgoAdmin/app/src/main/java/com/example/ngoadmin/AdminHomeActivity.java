package com.example.ngoadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PostItemClickListener, SearchView.OnQueryTextListener {



    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private ArrayList<PostModel> postModelArrayList;
    private PostAdapter postAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        recyclerView = findViewById(R.id.admin_home_post_list);

        postModelArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(AdminHomeActivity.this, postModelArrayList, this);
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
                        for (DataSnapshot postModelSnapshot : dataSnapshot.getChildren()) {
                            final PostModel postModel = postModelSnapshot.getValue(PostModel.class);

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

                            postModelArrayList.add(postModel);
                        }
                        postAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_volunteer) {
            final Intent addVolunteer = new Intent(AdminHomeActivity.this, AddVolunteerActivity.class);
            startActivity(addVolunteer);
        } else if (id == R.id.nav_add_address) {
            final Intent addArea = new Intent(AdminHomeActivity.this, AddAreaActivity.class);
            startActivity(addArea);
        } else if (id == R.id.nav_verify_user) {
            final Intent verifyUser = new Intent(AdminHomeActivity.this, VerifyUserActivity.class);
            startActivity(verifyUser);
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences;
            sharedpreferences = getSharedPreferences("login.xml", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("TYPE", "2");
            editor.apply();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostItemClickListener(PostModel postModel) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String usertext = s.toLowerCase();
        ArrayList<PostModel> searchArrayList = new ArrayList<>();

        for (int i = 0; i < postModelArrayList.size(); i++) {
            if (postModelArrayList.get(i).getTitle().toLowerCase().contains(usertext) ||
                    postModelArrayList.get(i).getTitle().toLowerCase().contains(usertext)) {
                searchArrayList.add(postModelArrayList.get(i));
            }
        }
        postAdapter.updateList(searchArrayList);
        return true;
    }
}
