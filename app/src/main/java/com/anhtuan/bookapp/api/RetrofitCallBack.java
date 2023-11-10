package com.anhtuan.bookapp.api;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;

import android.util.Log;

import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.RefreshTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallBack<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            int statusCode = response.code();
            if (statusCode == 403) {
                refreshTokenAndRetry(call);
            } else {
                onFailure("Request failed with code: " + statusCode);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure("Request failed: " + t.getMessage());
    }

    public abstract void onSuccess(T response);

    public abstract void onFailure(String errorMessage);

    private void refreshTokenAndRetry(Call<T> call) {
        String refreshToken = TokenManager.getInstance().getRefreshToken();
        unAuthApi.refreshToken(refreshToken).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call1, Response<RefreshTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 100) {
                    TokenManager.getInstance().updateToken(response.body().getData());
                    retryOriginalCall(call.clone());
                } else {
                    reLoginAndRetry(call.clone());
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call1, Throwable t) {
                Log.d("REFRESH-TOKEN", "FAIL");
            }
        });
    }

    private void retryOriginalCall(Call<T> call) {

        call.enqueue(this);
    }

    private void reLoginAndRetry(Call<T> call) {
        String email = AccountManager.getInstance().getEmail();
        String password = AccountManager.getInstance().getPassword();
        unAuthApi.reLogin(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call1, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 100) {
                    String newToken = response.body().getData().getToken();
                    String newRefreshToken = response.body().getData().getRefreshToken();
                    TokenManager.getInstance().saveToken(newToken, newRefreshToken);
                    retryOriginalCall(call.clone());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call1, Throwable t) {

            }
        });
    }
}
