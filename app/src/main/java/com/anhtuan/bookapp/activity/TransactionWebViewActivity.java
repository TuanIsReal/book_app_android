package com.anhtuan.bookapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityTransactionWebViewBinding;

public class TransactionWebViewActivity extends AppCompatActivity {

    ActivityTransactionWebViewBinding binding;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        if (!url.isBlank()){
            binding.webView.loadUrl(url);
            binding.webView.setWebViewClient(new WebViewClient());
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.setWebChromeClient(new WebChromeClient());
        } else {
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}