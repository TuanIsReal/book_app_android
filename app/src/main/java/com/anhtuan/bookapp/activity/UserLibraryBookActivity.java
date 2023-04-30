package com.anhtuan.bookapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anhtuan.bookapp.databinding.ActivityUserLibraryBookBinding;

public class UserLibraryBookActivity extends AppCompatActivity {

    ActivityUserLibraryBookBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLibraryBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}