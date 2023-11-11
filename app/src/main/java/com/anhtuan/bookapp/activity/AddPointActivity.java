package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.PaymentApi.paymentApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityAddPointBinding;
import com.anhtuan.bookapp.request.PaymentRequest;
import com.anhtuan.bookapp.response.CreatePaymentResponse;

public class AddPointActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100006;
    ActivityAddPointBinding binding;
    PaymentRequest request;
    String paymentUrl;
    Intent buyIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityAddPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        request = new PaymentRequest();
        request.setDescription("addPoint");

        buyIntent = new Intent(this, TransactionWebViewActivity.class);

        binding.pack50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPaymentUrl(50);
            }
        });

        binding.pack100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPaymentUrl(100);
            }
        });

        binding.pack200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPaymentUrl(200);
            }
        });

        binding.pack500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPaymentUrl(500);
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createPaymentUrl(int amount){
        request.setPoint(amount);

        paymentApi.createPayment(request).enqueue(new RetrofitCallBack<CreatePaymentResponse>() {
            @Override
            public void onSuccess(CreatePaymentResponse response) {
                paymentUrl = response.getData();
                buyIntent.putExtra("url", paymentUrl);
                startActivityForResult(buyIntent, REQUEST_CODE);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            onBackPressed();
        }
    }
}