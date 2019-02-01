package com.climo.al.climoproject.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.climo.al.climoproject.Akun;
import com.climo.al.climoproject.Logout;
import com.climo.al.climoproject.Home;
import com.climo.al.climoproject.Inbox;
import com.climo.al.climoproject.PostingSaya;
import com.climo.al.climoproject.R;
import com.climo.al.climoproject.TentangKami;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    FirebaseUser currentUSer;
    FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.tabmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView nvView = (NavigationView) findViewById(R.id.nv);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.Drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close );
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerLayout(nvView);


        //init
        mAuth = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser();
        Fragment myFragment = null;
        myFragment = (Fragment) Home.newInstance("","");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frm,myFragment).commit();
        updateNavHeader();

    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void selecItemDrawer(MenuItem menuItem) {
        Fragment myFragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()){
            case R.id.home:
                fragmentClass = Home.class;
                break;
            case R.id.akun:
                fragmentClass = Akun.class;
                break;
            case R.id.inbox:
                fragmentClass = Inbox.class;
                break;
            case R.id.post:
                fragmentClass = PostingSaya.class;
                break;
            case R.id.about:
                fragmentClass = TentangKami.class;
                break;
            case R.id.logout:
                fragmentClass = Logout.class;
                break;
             default:
                 fragmentClass = Home.class;
        }
        try
        {
          myFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frm,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerlayout.closeDrawers();

    }




    private void setupDrawerLayout(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selecItemDrawer(item);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navName);
        TextView navMail = headerView.findViewById(R.id.navEmail);
        ImageView navImg = headerView.findViewById(R.id.navImg);

        navUsername.setText(currentUSer.getDisplayName());
        navMail.setText(currentUSer.getEmail());
        Glide.with(this).load(currentUSer.getPhotoUrl()).into(navImg);


    }


    private void senToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}