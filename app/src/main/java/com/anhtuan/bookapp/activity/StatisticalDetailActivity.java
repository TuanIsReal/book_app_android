package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.StatApi.statApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterRankingUser;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityStatisticalDetailBinding;
import com.anhtuan.bookapp.response.RankingBookResponse;
import com.anhtuan.bookapp.response.RankingUserResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalDetailActivity extends AppCompatActivity {
    ActivityStatisticalDetailBinding binding;
    AdapterRankingUser adapterRankingUser;
    int typeRanking;
    Map<String, Double> rankingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityStatisticalDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        typeRanking = intent.getIntExtra("typeRanking", 1);
        loadDataRanking();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataRanking();
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadDataRanking() {
        if (typeRanking < 4){
            statApi.getRankingUser(typeRanking).enqueue(new RetrofitCallBack<RankingUserResponse>() {
                @Override
                public void onSuccess(RankingUserResponse response) {
                    binding.swipeRefresh.setRefreshing(false);
                    if (response != null && response.getCode() == 100){
                        rankingData = response.getData();
                        adapterRankingUser = new AdapterRankingUser(StatisticalDetailActivity.this, rankingData, 1);
                        binding.rankList.setAdapter(adapterRankingUser);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    binding.swipeRefresh.setRefreshing(false);
                }
            });
        } else {
            statApi.getRankingBook().enqueue(new RetrofitCallBack<RankingBookResponse>() {
                @Override
                public void onSuccess(RankingBookResponse response) {
                    binding.swipeRefresh.setRefreshing(false);
                    if (response != null && response.getCode() == 100){
                        rankingData = response.getData();
                        adapterRankingUser = new AdapterRankingUser(StatisticalDetailActivity.this, rankingData, 2);
                        binding.rankList.setAdapter(adapterRankingUser);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    binding.swipeRefresh.setRefreshing(false);
                }
            });
        }

    }
}