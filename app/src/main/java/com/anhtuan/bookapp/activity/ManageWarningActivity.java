package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;
import static com.anhtuan.bookapp.api.WarningApi.warningApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.anhtuan.bookapp.adapter.AdapterManageWarning;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityManageWarningBinding;
import com.anhtuan.bookapp.domain.WarningChapter;
import com.anhtuan.bookapp.response.GetWarningListData;
import com.anhtuan.bookapp.response.GetWarningListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;


public class ManageWarningActivity extends AppCompatActivity implements AdapterManageWarning.ManageWarningListener {

    ActivityManageWarningBinding binding;
    List<GetWarningListData> warningChapterList;
    AdapterManageWarning adapterManageWarning;
    private final static int REQUEST_CODE = 100017;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageWarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadWarningChapter();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWarningChapter();
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        binding.manageCategoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.manageBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, ManageBookActivity.class));
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, StatisticalActivity.class));
            }
        });

        binding.manageRequestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, ManageRequestBookActivity.class));
            }
        });
    }

    private void loadWarningChapter() {
        warningApi.getWarningList().enqueue(new RetrofitCallBack<GetWarningListResponse>() {
            @Override
            public void onSuccess(GetWarningListResponse response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response != null){
                    if (response.getCode() == 100){
                        warningChapterList = response.getData();
                        loadWarningChapterRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadWarningChapterRecycleView() {
        adapterManageWarning = new AdapterManageWarning(ManageWarningActivity.this, warningChapterList);
        adapterManageWarning.setManageWarningListener(ManageWarningActivity.this);
        binding.warningChapterRv.setAdapter(adapterManageWarning);
    }


    private void logout(){
        userApi.logout().enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(ManageWarningActivity.this, MainActivity.class));
                finish();
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
            loadWarningChapter();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("onItemClick", "true");
        GetWarningListData warningChapter = warningChapterList.get(position);
        Intent intent = new Intent(view.getContext(), WarningDetailActivity.class);
        intent.putExtra("chapter", warningChapter.getChapter());
        intent.putExtra("chapterReport", warningChapter.getChapterReport());
        intent.putExtra("book", warningChapter.getBook());
        intent.putExtra("bookReport", warningChapter.getBookReport());
        startActivityForResult(intent, REQUEST_CODE);
    }
}