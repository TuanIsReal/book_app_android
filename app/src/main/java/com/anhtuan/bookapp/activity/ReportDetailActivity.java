package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.ReportApi.reportApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityReportDetailBinding;
import com.anhtuan.bookapp.domain.Report;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;

public class ReportDetailActivity extends AppCompatActivity {

    ActivityReportDetailBinding binding;
    Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityReportDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        report = (Report) intent.getSerializableExtra("report");

        if (report != null){
            setReportDetail();
        }

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setReportDetail() {
        binding.content.setText(report.getContent());
        int type = report.getType();
        switch (type){
            case 1:
                binding.type.setText("Nội dung người lớn");
                break;
            case 2:
                binding.type.setText("Gây thù hằn");
                break;
            case 3:
                binding.type.setText("Công kích cá nhân");
                break;
            case 4:
                binding.type.setText("Spam");
                break;
            case 5:
                binding.type.setText("Các vấn đề khác");
                break;
        }

        if (report.getBookId() != null){
            bookApi.getBookById(report.getBookId()).enqueue(new RetrofitCallBack<ViewBookResponse>() {
                @Override
                public void onSuccess(ViewBookResponse response) {
                    if (response.getCode() == 100){
                        binding.bookName.setText(response.getData().getBookName());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }

        if (report.getUserId() != null){
            userApi.getUser(report.getUserId()).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
                @Override
                public void onSuccess(GetUserInfoResponse response) {
                    if (response.getCode() == 100){
                        binding.userName.setText(response.getData().getName());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }

        binding.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportApi.updateReport(report.getId(), 1).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response.getCode() == 100){
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        });
    }
}