package com.anhtuan.bookapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityTransactionWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        if (!url.isBlank()){
            binding.webView.loadUrl(url);
            binding.webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Log.d("url request", request.getUrl().toString());
                    if (request.getUrl().toString().equals("app://back")) {
                        setResult(RESULT_OK);
                        finish();
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
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