package com.anhtuan.bookapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.anhtuan.bookapp.fragment.HomeFragment;
import com.anhtuan.bookapp.fragment.NotificationFragment;
import com.anhtuan.bookapp.fragment.ProfileFragment;
import com.anhtuan.bookapp.fragment.UserBookLibraryFragment;

public class AdapterViewDashboardUser extends FragmentStatePagerAdapter {

    int numPage;
    public AdapterViewDashboardUser(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numPage = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new UserBookLibraryFragment();
            case 2:
                return new NotificationFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new HomeFragment();

        }

    }

    @Override
    public int getCount() {
        return numPage;
    }
}
