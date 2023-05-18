package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterBalanceChange;
import com.anhtuan.bookapp.databinding.ActivityBalanceChangeBinding;
import com.anhtuan.bookapp.domain.TransactionHistory;
import com.anhtuan.bookapp.response.GetBalanceChangeResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceChangeActivity extends AppCompatActivity {

    ActivityBalanceChangeBinding binding;
    List<TransactionHistory>  transactionHistoryList;
    AdapterBalanceChange adapterBalanceChange;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBalanceChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        loadTransactionHistory();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTransactionHistory();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadTransactionHistory() {
        userApi.getBalanceChange(userId).enqueue(new Callback<GetBalanceChangeResponse>() {
            @Override
            public void onResponse(Call<GetBalanceChangeResponse> call, Response<GetBalanceChangeResponse> response) {
                binding.swipeRefresh.setRefreshing(false);
                if (!Objects.isNull(response.body()) && response.body().getCode() == 100){
                    transactionHistoryList = response.body().getData();
                    adapterBalanceChange  = new AdapterBalanceChange(BalanceChangeActivity.this,  transactionHistoryList);
                    binding.balanceChangeRv.setAdapter(adapterBalanceChange);
                }
            }

            @Override
            public void onFailure(Call<GetBalanceChangeResponse> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(BalanceChangeActivity.this,""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}