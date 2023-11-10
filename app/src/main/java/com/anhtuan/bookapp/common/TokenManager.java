package com.anhtuan.bookapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "token_pref";
    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";
    public static TokenManager INSTANCE = new TokenManager();

    private SharedPreferences preferences;

    public TokenManager() {
    }

    public static TokenManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token, String refreshToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public void updateToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return preferences.getString(TOKEN, null);
    }

    public String getRefreshToken() {
        return preferences.getString(REFRESH_TOKEN, null);
    }

    public void deleteToken() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.apply();
    }
}
