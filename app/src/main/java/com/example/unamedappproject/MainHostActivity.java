package com.example.unamedappproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainHostActivity extends AppCompatActivity implements fragmentLeft.OnFragmentInteractionListener,fragmentRight.OnFragmentInteractionListener {
    public Fragment mainHostFragment;
    NavHostFragment navHostFragment;
    NavController navController;
    ViewPager mPager;
    PagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);

//         navHostFragment =
//                (NavHostFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.fragNavHost);
        // navController = navHostFragment.getNavController();
//
        bottomNavigationView = findViewById(R.id.bottom_navigation);
      bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
       // NavigationUI.setupWithNavController(bottomNavigationView, navController);


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onPageSelected(int position) {
              switch (position){
                  case 0:
                      bottomNavigationView.getMenu().findItem(R.id.nav_left).setChecked(true);
                      break;
                  case 1:
                      bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                      break;
                  case 2:
                      bottomNavigationView.getMenu().findItem(R.id.nav_right).setChecked(true);
                      break;
              }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.nav_home:
                            mPager.setCurrentItem(1);
                            break;
                        case R.id.nav_left:
                            mPager.setCurrentItem(0);
                            break;
                        case R.id.nav_right:
                            mPager.setCurrentItem(2);
                            break;
                    }
                    return true;
                }

            };
}
