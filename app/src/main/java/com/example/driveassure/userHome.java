package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class userHome extends AppCompatActivity {
    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        pagerMain = findViewById(R.id.pagerMain);
        bottomNav = findViewById(R.id.bottomNav);

        fragmentArrayList.add(new homeUserFragment());
        fragmentArrayList.add(new renterUserFragment());
        fragmentArrayList.add(new ownerUserFragment());
        fragmentArrayList.add(new profileUserFragment());

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);

        // Set Adapter

        pagerMain.setAdapter(adapterViewPager);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        bottomNav.setSelectedItemId(R.id.navHome);
                        break;

                    case 1:
                        bottomNav.setSelectedItemId(R.id.navRenter);
                        break;

                    case 2:
                        bottomNav.setSelectedItemId(R.id.navOwner);
                        break;
                    case 3:
                        bottomNav.setSelectedItemId(R.id.navProfile);
                        break;

                }
                super.onPageSelected(position);
            }
        });
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    pagerMain.setCurrentItem(0);
                } else if (item.getItemId() == R.id.navRenter) {
                    pagerMain.setCurrentItem(1);
                } else if (item.getItemId() == R.id.navOwner) {
                    pagerMain.setCurrentItem(2);
                } else if (item.getItemId() == R.id.navProfile) {
                    pagerMain.setCurrentItem(3);
                }
                return true;
            }
        });


    }
}