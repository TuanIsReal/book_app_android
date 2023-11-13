package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.AddPointActivity;
import com.anhtuan.bookapp.activity.BalanceChangeActivity;
import com.anhtuan.bookapp.activity.IncomeActivity;
import com.anhtuan.bookapp.activity.MainActivity;
import com.anhtuan.bookapp.activity.SettingActivity;
import com.anhtuan.bookapp.activity.SplashActivity;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
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
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();

        loadUser();

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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

    private void logout(){
        userApi.logout().enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadUser(){
        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 122){
                    AccountManager.getInstance().logoutAccount();
                    TokenManager.getInstance().deleteToken();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    getActivity().finish();

                } else if (response.getCode() == 100){
                    User user = response.getData();
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
                        unAuthApi.getAvatar(imageName).enqueue(new Callback<ImageResponse>() {
                            @Override
                            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                if (response.body().getCode() == 100){
                                    String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                                    Glide.with(view.getContext())
                                            .load(path)
                                            .into(avatar);
                                }
                            }

                            @Override
                            public void onFailure(Call<ImageResponse> call, Throwable t) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}