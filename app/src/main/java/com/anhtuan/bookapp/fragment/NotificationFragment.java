package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.NotificationApi.notificationApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.MainActivity;
import com.anhtuan.bookapp.adapter.AdapterNotification;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.domain.Notification;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetNotificationResponse;
import java.util.List;


public class NotificationFragment extends Fragment {

    View view;
    RecyclerView notificationsRv;
    AdapterNotification adapterNotification;
    SwipeRefreshLayout swipeRefresh;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationsRv = view.findViewById(R.id.notificationsRv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

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

        loadNotifications();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotifications();
            }
        });

        return view;
    }

    private void loadNotifications() {
        notificationApi.getNotification().enqueue(new RetrofitCallBack<GetNotificationResponse>() {
            @Override
            public void onSuccess(GetNotificationResponse response) {
                swipeRefresh.setRefreshing(false);
                if (response!= null && response.getCode() == 100){
                    List<Notification> notificationList = response.getData();
                    adapterNotification = new AdapterNotification(view.getContext(), notificationList);
                    notificationsRv.setAdapter(adapterNotification);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}