package com.anhtuan.bookapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.View;

import com.anhtuan.bookapp.databinding.ActivityUserLibraryBookBinding;

public class UserLibraryBookActivity extends AppCompatActivity {

    ActivityUserLibraryBookBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUserLibraryBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}