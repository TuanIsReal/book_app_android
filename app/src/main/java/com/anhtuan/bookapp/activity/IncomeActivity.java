package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.StatApi.statApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityIncomeBinding;
import com.anhtuan.bookapp.domain.TransactionHistory;
import com.anhtuan.bookapp.response.IncomeMemberData;
import com.anhtuan.bookapp.response.IncomeMemberResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeActivity extends AppCompatActivity {

    ArrayList<BarEntry> dataRevenue, dataSpend, dataChange;
    ActivityIncomeBinding binding;
    List<TransactionHistory> transactionHistoryList;
    String start, end;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        binding.resultStat.setVisibility(View.GONE);
        binding.revenueChart.setVisibility(View.GONE);
        binding.spendChart.setVisibility(View.GONE);
        binding.changeChart.setVisibility(View.GONE);

        binding.chooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(IncomeActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                binding.month.setText((selectedMonth + 1) + "/" + selectedYear);
                                start = "01-" + (selectedMonth + 1) + "-" + selectedYear;
                                if ((selectedMonth + 1) == 12){
                                    end = "01-01-" + (selectedYear + 1);
                                } else {
                                    end = "01-" + (selectedMonth + 2) + "-" + selectedYear;
                                }
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.MAY)
                        .setMinYear(2022)
                        .setActivatedYear(2023)
                        .setMaxYear(2024)
                        .setTitle("Chọn tháng năm")
                        .build().show();
            }
        });

        binding.statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.isNull(start) || start.isBlank()){
                    Toast.makeText(IncomeActivity.this, "Chưa chọn thời gian thống kê", Toast.LENGTH_SHORT).show();
                } else {
                    statApi.incomeMember(userId, start, end).enqueue(new Callback<IncomeMemberResponse>() {
                        @Override
                        public void onResponse(Call<IncomeMemberResponse> call, Response<IncomeMemberResponse> response) {
                            binding.resultStat.setVisibility(View.VISIBLE);
                            if (response.body() != null && response.body().getCode() == 100){
                                IncomeMemberData data = response.body().getData();
                                binding.revenue.setText(String.valueOf(data.getRevenue()) + " point");
                                binding.spend.setText(String.valueOf(data.getSpend()) + " point");
                                binding.change.setText(String.valueOf(data.getChange()) + " point");
                                transactionHistoryList = data.getTransactionHistoryList();
                            }
                        }

                        @Override
                        public void onFailure(Call<IncomeMemberResponse> call, Throwable t) {
                            Toast.makeText(IncomeActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        binding.exportChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();

            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initData() {
        dataRevenue = new ArrayList<BarEntry>();
        dataSpend = new ArrayList<BarEntry>();
        dataChange = new ArrayList<BarEntry>();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate, endDate;
        try {
            startDate = format.parse(start);
            endDate = format.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long milis = startDate.getTime();
        long endMilis = endDate.getTime();
        int date = 1;


        while (milis < endMilis){
            int revenueDate = 0, spendDate = 0;
            for (TransactionHistory transactionHistory: transactionHistoryList){
                if (transactionHistory.getTransactionTime() > milis + 86400000){
                    break;
                }
                if (transactionHistory.getTransactionType() == 2 && transactionHistory.getTransactionTime() > milis){
                    revenueDate += transactionHistory.getPoint();
                }
                if (transactionHistory.getTransactionType() == 1 && transactionHistory.getTransactionTime() > milis){
                    spendDate -= transactionHistory.getPoint();
                }
            }
            dataRevenue.add(new BarEntry(date, revenueDate));
            dataSpend.add(new BarEntry(date, spendDate));
            dataChange.add(new BarEntry(date, revenueDate - spendDate));
            milis += 86400000;
            date += 1;
        }
        initChart();
    }

    private void initChart(){
        binding.revenueChart.setVisibility(View.VISIBLE);
        binding.spendChart.setVisibility(View.VISIBLE);
        binding.changeChart.setVisibility(View.VISIBLE);

        BarDataSet barDataSetRevenue = new BarDataSet(dataRevenue, "Tổng thu trong ngày");
        BarDataSet barDataSetSpend = new BarDataSet(dataSpend, "Tổng chi trong ngày");
        BarDataSet barDataSetChange = new BarDataSet(dataChange, "Biến động trong ngày");

        barDataSetRevenue.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetRevenue.setValueTextColor(Color.BLACK);
        barDataSetRevenue.setValueTextSize(16f);

        barDataSetSpend.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetSpend.setValueTextColor(Color.BLACK);
        barDataSetSpend.setValueTextSize(16f);

        barDataSetChange.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetChange.setValueTextColor(Color.BLACK);
        barDataSetChange.setValueTextSize(16f);

        BarData barDataRevenue = new BarData(barDataSetRevenue);
        BarData barDataSpend = new BarData(barDataSetSpend);
        BarData barDataChange = new BarData(barDataSetChange);

        binding.revenueChart.setData(barDataRevenue);
        binding.revenueChart.getDescription().setText("Biểu đồ thu nhập trong tháng");
        binding.revenueChart.animateY(2000);

        binding.spendChart.setData(barDataSpend);
        binding.spendChart.getDescription().setText("Biểu đồ chi tiêu trong tháng");
        binding.spendChart.animateY(2000);

        binding.changeChart.setData(barDataChange);
        binding.changeChart.getDescription().setText("Biểu đồ biến động trong tháng");
        binding.changeChart.animateY(2000);
    }
}