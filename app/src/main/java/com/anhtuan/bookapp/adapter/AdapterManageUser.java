package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.activity.UserDetailActivity;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowUserManagerBinding;
import com.anhtuan.bookapp.domain.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterManageUser extends RecyclerView.Adapter<AdapterManageUser.HolderManageUser>{

    private Context context;
    private List<User> userList;
    private RowUserManagerBinding binding;

    public AdapterManageUser(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public HolderManageUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        binding = RowUserManagerBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderManageUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageUser holder, int position) {
        User user = userList.get(position);
        holder.nameTv.setText(user.getName());
        holder.userIdTv.setText(user.getId());
        holder.emailTv.setText(user.getEmail());
        holder.totalPointTv.setText(String.valueOf(user.getPoint()) + " Point");
        if (user.getAvatarImage() != null && !user.getAvatarImage().isBlank()) {
            if (user.getGoogleLogin() != null && user.getGoogleLogin()){
                Glide.with(context).load(user.getAvatarImage())
                        .signature(new ObjectKey(user.getAvatarImage()))
                        .into(holder.avatar);
            } else {
                String path = Constant.IP_SERVER_IMAGE + user.getAvatarImage();
                Glide.with(context)
                        .load(path)
                        .signature(new ObjectKey(path))
                        .into(holder.avatar);
            }

        }
        if (user.getStatus() == 1){
            holder.statusTv.setText("Hoạt động ");
        } else if (user.getStatus() == -1) {
            holder.statusTv.setText("Đang khóa ");
        } else {
            holder.statusTv.setText("Không hoạt động ");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("userId", user.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderManageUser extends RecyclerView.ViewHolder{

        TextView nameTv, userIdTv, totalPointTv, statusTv, emailTv;
        CircleImageView avatar;

        public HolderManageUser(@NonNull View itemView) {
            super(itemView);
            nameTv = binding.nameTv;
            userIdTv = binding.userIdTv;
            totalPointTv = binding.totalPointTv;
            statusTv = binding.statusTv;
            emailTv = binding.emailTv;
            avatar = binding.avatar;
        }
    }
}
