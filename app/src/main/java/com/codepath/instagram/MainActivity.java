package com.codepath.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.codepath.instagram.fragments.PostFragment;
import com.codepath.instagram.fragments.ProfileFragment;
import com.codepath.instagram.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.io.File;
import java.sql.Time;

/* Activity in charge of the bottom navigation menu; controls which fragment is viewed */

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private ImageButton ibLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ibLogout = findViewById(R.id.ibLogout);

        //sets up the bottom navigation menu such that the correct fragments are displayed
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home: //timeline fragment
                        fragment = new TimelineFragment();
                        break;
                    case R.id.action_compose: //post fragment
                        fragment = new PostFragment();
                        break;
                    case R.id.action_profile: //profile fragment
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new TimelineFragment();
                        break;
                }
                //replaced the contained with the correct fragment
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        //setting up log out button to allow user to log out
        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log out user account
                ParseUser.logOut();

                //create intent back to login page
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        //set default fragment selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}