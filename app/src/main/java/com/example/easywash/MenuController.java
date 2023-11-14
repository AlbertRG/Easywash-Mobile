package com.example.easywash;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MenuController extends FragmentStatePagerAdapter {

    int numOptions;

    public MenuController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numOptions = behavior;
    }//MenuController

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                // Aquí establece la etiqueta "CarsFragment" al agregar el fragmento
                CarsFragment carsFragment = new CarsFragment();
                return carsFragment;
            case 2: return new SettingFragment();
            default: return new HomeFragment();
        }
    }//getItem

    @Override
    public int getCount() {
        return numOptions;
    }//getCount

}//MenuController
