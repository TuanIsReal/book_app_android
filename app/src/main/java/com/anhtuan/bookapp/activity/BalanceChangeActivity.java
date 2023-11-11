package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterBalanceChange;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityBalanceChangeBinding;
import com.anhtuan.bookapp.domain.TransactionHistory;
import com.anhtuan.bookapp.response.GetBalanceChangeResponse;

import java.util.List;

public class BalanceChangeActivity extends AppCompatActivity {

    ActivityBalanceChangeBinding binding;
    List<TransactionHistory>  transactionHistoryList;
    AdapterBalanceChange adapterBalanceChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityBalanceChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        userApi.getBalanceChange().enqueue(new RetrofitCallBack<GetBalanceChangeResponse>() {
            @Override
            public void onSuccess(GetBalanceChangeResponse response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.getCode() == 100){
                    transactionHistoryList = response.getData();
                    adapterBalanceChange  = new AdapterBalanceChange(BalanceChangeActivity.this,  transactionHistoryList);
                    binding.balanceChangeRv.setAdapter(adapterBalanceChange);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(BalanceChangeActivity.this,errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }
}