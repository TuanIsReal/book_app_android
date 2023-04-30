package com.anhtuan.bookapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.anhtuan.bookapp.fragment.HistoryReadBookFragment;
import com.anhtuan.bookapp.fragment.UserUploadBookFragment;

public class AdapterUserBookLibrary extends FragmentStatePagerAdapter {
    int numPage;
    String userId;
    public AdapterUserBookLibrary(@NonNull FragmentManager fm, int behavior, String userId) {
        super(fm, behavior);
        numPage = behavior;
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HistoryReadBookFragment();
            case 1:
                return new UserUploadBookFragment();
            default:
                return new HistoryReadBookFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Lịch sử";
            case 1:
                return "Truyện đăng";
            default:
                return "Lịch sử";
        }
    }
}
