package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.NotificationApi.notificationApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowNotificationBinding;
import com.anhtuan.bookapp.domain.Notification;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification>{
    private Context context;
    public List<Notification> notificationList;
    public RowNotificationBinding binding;

    public AdapterNotification(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public HolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowNotificationBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderNotification(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNotification holder, int position) {
        Notification notification = notificationList.get(position);
        String bookId = notification.getBookId();
        String body = notification.getBody();
        long time = notification.getTime();
        boolean isClick = notification.isClick();

        if (isClick){
            holder.contentRl.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.messageTv.setText(body);
        holder.timeTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClick){
                    notificationApi.clickNotification(notification.getId()).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                holder.contentRl.setBackgroundColor(context.getResources().getColor(R.color.white));
                                notification.setClick(true);
                                if (!Objects.isNull(bookId) && !bookId.isBlank()){
                                    Intent intent = new Intent(context, ViewBookActivity.class);
                                    intent.putExtra("bookId", bookId);
                                    context.startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    class HolderNotification extends RecyclerView.ViewHolder{
        TextView messageTv, timeTv;
        RelativeLayout contentRl;
        public HolderNotification(@NonNull View itemView) {
            super(itemView);
            messageTv = binding.messageTv;
            timeTv = binding.timeTv;
            contentRl = binding.contentRl;
        }
    }
}
