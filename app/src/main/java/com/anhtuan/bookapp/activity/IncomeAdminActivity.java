package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.StatApi.statApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityIncomeAdminBinding;
import com.anhtuan.bookapp.domain.Payment;
import com.anhtuan.bookapp.domain.TransactionHistory;
import com.anhtuan.bookapp.response.IncomeAdminData;
import com.anhtuan.bookapp.response.IncomeAdminResponse;
import com.anhtuan.bookapp.response.IncomeMemberData;
import com.anhtuan.bookapp.response.IncomeMemberResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
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

public class IncomeAdminActivity extends AppCompatActivity {

    ActivityIncomeAdminBinding binding;
    ArrayList<BarEntry> dataPoint, dataMoney, dataTransaction;
    String start, end;
    List<Payment> paymentList;
    public static final long TIME_INTERVAL = 3000;
    long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityIncomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.resultStat.setVisibility(View.GONE);
        binding.pointChart.setVisibility(View.GONE);
        binding.moneyChart.setVisibility(View.GONE);
        binding.transactionChart.setVisibility(View.GONE);

        binding.chooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(IncomeAdminActivity.this,
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
                    Toast.makeText(IncomeAdminActivity.this, "Chưa chọn thời gian thống kê", Toast.LENGTH_SHORT).show();
                } else {
                    statApi.incomeAdmin(start, end).enqueue(new Callback<IncomeAdminResponse>() {
                        @Override
                        public void onResponse(Call<IncomeAdminResponse> call, Response<IncomeAdminResponse> response) {
                            binding.resultStat.setVisibility(View.VISIBLE);
                            if (response.body() != null && response.body().getCode() == 100){
                                IncomeAdminData data = response.body().getData();
                                binding.point.setText(String.valueOf(data.getTotalPoint()) + " point");
                                binding.money.setText(String.valueOf(data.getTotalMoney()) + " VND");
                                binding.transactionPoint.setText(String.valueOf(data.getTransactionPoint()) + " point");
                                paymentList = data.getPaymentList();
                                binding.transaction.setText(String.valueOf(paymentList.size()));
                            }
                        }

                        @Override
                        public void onFailure(Call<IncomeAdminResponse> call, Throwable t) {
                            Toast.makeText(IncomeAdminActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        dataMoney = new ArrayList<BarEntry>();
        dataPoint = new ArrayList<BarEntry>();
        dataTransaction = new ArrayList<BarEntry>();

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
            int pointDate = 0;
            long moneyDate = 0;
            int transaction = 0;

            for (Payment payment: paymentList){
                if (payment.getPayTime() > milis + 86400000){
                    break;
                }
                if (payment.getPayTime() > milis){
                    pointDate += payment.getPoint();
                    moneyDate +=  payment.getMoney();
                    transaction += 1;
                }
            }
            dataMoney.add(new BarEntry(date, moneyDate));
            dataPoint.add(new BarEntry(date, pointDate));
            dataTransaction.add(new BarEntry(date, transaction));
            milis += 86400000;
            date += 1;
        }
        initChart();
    }

    private void initChart(){
        binding.pointChart.setVisibility(View.VISIBLE);
        binding.moneyChart.setVisibility(View.VISIBLE);
        binding.transactionChart.setVisibility(View.VISIBLE);

        BarDataSet barDataSetPoint = new BarDataSet(dataPoint, "Tổng point nạp trong ngày");
        BarDataSet barDataSetMoney = new BarDataSet(dataMoney, "Tổng tiền nạp trong ngày");
        BarDataSet barDataSetTransaction = new BarDataSet(dataTransaction, "Tổng lượt giao dịch nạp trong ngày");

        barDataSetPoint.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetPoint.setValueTextColor(Color.BLACK);
        barDataSetPoint.setValueTextSize(16f);

        barDataSetMoney.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetMoney.setValueTextColor(Color.BLACK);
        barDataSetMoney.setValueTextSize(16f);

        barDataSetTransaction.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetTransaction.setValueTextColor(Color.BLACK);
        barDataSetTransaction.setValueTextSize(16f);

        BarData barDataPoint = new BarData(barDataSetPoint);
        BarData barDataMoney = new BarData(barDataSetMoney);
        BarData barDataTransaction = new BarData(barDataSetTransaction);

        binding.pointChart.setData(barDataPoint);
        binding.pointChart.getDescription().setText("Biểu đồ thu nhập trong tháng");
        binding.pointChart.animateY(2000);

        binding.moneyChart.setData(barDataMoney);
        binding.moneyChart.getDescription().setText("Biểu đồ chi tiêu trong tháng");
        binding.moneyChart.animateY(2000);

        binding.transactionChart.setData(barDataTransaction);
        binding.transactionChart.getDescription().setText("Biểu đồ lượt giao dịch nạp trong tháng");
        binding.transactionChart.animateY(2000);
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(IncomeAdminActivity.this, "Quay lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}