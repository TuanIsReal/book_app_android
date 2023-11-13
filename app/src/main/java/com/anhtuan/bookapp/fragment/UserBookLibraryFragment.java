package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.MainActivity;
import com.anhtuan.bookapp.adapter.AdapterUserBookLibrary;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.google.android.material.tabs.TabLayout;



public class UserBookLibraryFragment extends Fragment {

    public UserBookLibraryFragment() {
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
        AdapterUserBookLibrary adapter = new AdapterUserBookLibrary(getChildFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);

        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 122 || response.getCode() == 106){
                    AccountManager.getInstance().logoutAccount();
                    TokenManager.getInstance().deleteToken();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });

        return view;
    }
}