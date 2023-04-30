package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.NotificationApi.notificationApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterNotification;
import com.anhtuan.bookapp.domain.Notification;
import com.anhtuan.bookapp.response.GetNotificationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {

    View view;
    RecyclerView notificationsRv;
    AdapterNotification adapterNotification;
    String userId;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        notificationsRv = view.findViewById(R.id.notificationsRv);

        notificationApi.getNotification(userId).enqueue(new Callback<GetNotificationResponse>() {
            @Override
            public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    List<Notification> notificationList = response.body().getData();
                    adapterNotification = new AdapterNotification(view.getContext(), notificationList);
                    notificationsRv.setAdapter(adapterNotification);
                }
            }

            @Override
            public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
                Toast.makeText(view.getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}