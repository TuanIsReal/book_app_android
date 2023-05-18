package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.UserApi.userApi;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.AddPointActivity;
import com.anhtuan.bookapp.activity.BalanceChangeActivity;
import com.anhtuan.bookapp.activity.IncomeActivity;
import com.anhtuan.bookapp.activity.MainActivity;
import com.anhtuan.bookapp.activity.SettingActivity;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    String userId;
    ImageButton reloadBtn;
    Button buyPointBtn;
    TextView nameTv, pointTv, logoutTv, settingTv, incomeTv, changeTv;
    CircleImageView avatar;
    public SharedPreferences sharedPreferences;
    View view;
    public ProfileFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        sharedPreferences = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        loadUser();

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(view.getContext(), userId);
            }
        });

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUser();
            }
        });

        buyPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddPointActivity.class);
                intent.putExtra("userId",userId);
                view.getContext().startActivity(intent);
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SettingActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        incomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), IncomeActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        changeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), BalanceChangeActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void initView() {
        nameTv = view.findViewById(R.id.nameTv);
        pointTv = view.findViewById(R.id.pointTv);
        avatar = view.findViewById(R.id.avatar);
        logoutTv = view.findViewById(R.id.logoutTv);
        settingTv = view.findViewById(R.id.settingTv);
        reloadBtn = view.findViewById(R.id.reloadBtn);
        buyPointBtn = view.findViewById(R.id.buyPointBtn);
        incomeTv = view.findViewById(R.id.incomeTv);
        changeTv = view.findViewById(R.id.changeTv);
    }

    private void logout(Context context, String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", "");
        editor.putString("role", "");
        editor.apply();

        userApi.logout(userId).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                getActivity().finish();
                startActivity(new Intent(context, MainActivity.class));
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                Toast.makeText(context, ""+ t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUser(){
        userApi.getUserInfo(userId).enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                GetUserInfoResponse responseBody = response.body();
                if (responseBody.getCode() == 100){
                    User user = responseBody.getData();
                    nameTv.setText(user.getName());
                    pointTv.setText(""+user.getPoint());
                    String imageName = user.getAvatarImage();
                    Boolean isLoginGoogle = user.getGoogleLogin();

                    if (!Objects.isNull(isLoginGoogle) && isLoginGoogle){

                        Glide.with(view.getContext())
                                .load(imageName)
                                .into(avatar);

                    }

                    if (imageName != null && Objects.isNull(isLoginGoogle)){
                        userApi.getAvatarImage(imageName).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    try {
                                        byte[] bytes = response.body().bytes();
                                        Glide.with(view.getContext())
                                                .load(bytes)
                                                .into(avatar);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("err", "err--fail");
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {

            }
        });
    }
}