package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainHostActivity extends AppCompatActivity implements fragmentLeft.OnFragmentInteractionListener,fragmentRight.OnFragmentInteractionListener {
    public Fragment mainHostFragment;
    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);

         navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragNavHost);
         navController = navHostFragment.getNavController();
//
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch(item.getItemId()){
//                        case R.id.nav_home:
//                            break;
//                        case R.id.nav_left:
//                            break;
//
//
//                    }
//                }
//            };
}
