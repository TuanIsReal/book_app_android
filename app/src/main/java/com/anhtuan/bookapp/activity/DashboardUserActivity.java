package com.anhtuan.bookapp.activity;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterViewDashboardUser;
import com.anhtuan.bookapp.databinding.ActivityDashboardUserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardUserActivity extends AppCompatActivity {

    private ActivityDashboardUserBinding binding;
    public SharedPreferences sharedPreferences;
    public static final long TIME_INTERVAL = 3000;
    long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");


        AdapterViewDashboardUser adapterDashboardUser = new AdapterViewDashboardUser(getSupportFragmentManager(), 4, userId);
        binding.viewPager.setAdapter(adapterDashboardUser);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_menu_tab1:
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.main_menu_tab2:
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.main_menu_tab3:
                        binding.viewPager.setCurrentItem(2);
                        break;
                    case R.id.main_menu_tab4:
                        binding.viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.main_menu_tab1).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.main_menu_tab2).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.main_menu_tab3).setChecked(true);
                        break;
                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.main_menu_tab4).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(DashboardUserActivity.this, "Quay lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}