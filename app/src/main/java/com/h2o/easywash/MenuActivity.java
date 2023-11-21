package com.h2o.easywash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {

    TabLayout menu;
    ViewPager fragment;
    MenuController menuCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu = findViewById(R.id.tabLayout);
        fragment = findViewById(R.id.vwpFragment);

        menuCtrl = new MenuController(getSupportFragmentManager(), menu.getTabCount());
        fragment.setAdapter(menuCtrl);
        menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragment.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                    case 1:
                    case 2:
                        menuCtrl.notifyDataSetChanged(); break;
                }
            }//onTabSelected

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }//onTabUnselected

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }//onTabReselected
        });

        String name;
        SharedPreferences metadata = getSharedPreferences("user.dat",MODE_PRIVATE);
        name = metadata.getString("name",null);

        //Associate the menu with the Viewpager
        fragment.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(menu));

    }//onCreate

}