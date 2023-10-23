package com.anhtuan.bookapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterUserBookLibrary;
import com.google.android.material.tabs.TabLayout;



public class UserBookLibraryFragment extends Fragment {

    String userId;

    public UserBookLibraryFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        View view = inflater.inflate(R.layout.fragment_user_book_library, container, false);
        TabLayout tab;
        ViewPager viewPager;

        tab = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);
        AdapterUserBookLibrary adapter = new AdapterUserBookLibrary(getChildFragmentManager(), 2, userId);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);

        return view;
    }
}