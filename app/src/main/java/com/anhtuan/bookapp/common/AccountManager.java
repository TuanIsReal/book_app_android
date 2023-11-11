package com.anhtuan.bookapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountManager {
    private static final String PREF_NAME = "account_pref";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    public static AccountManager INSTANCE = new AccountManager();

    private SharedPreferences preferences;

    public AccountManager() {
    }

    public static AccountManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAccount(String email, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public void saveRole(int role){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ROLE, role);
        editor.apply();
    }

    public void logoutAccount() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(EMAIL);
        editor.remove(PASSWORD);
        editor.remove(ROLE);
        editor.apply();
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public String getPassword() {
        return preferences.getString(PASSWORD, "");
    }

    public int getRole(){
        return preferences.getInt(ROLE, 1);
    }
}
