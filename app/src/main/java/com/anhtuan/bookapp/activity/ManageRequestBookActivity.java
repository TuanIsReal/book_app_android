package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterManageRequestBook;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityManageRequestBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetRequestUploadBookResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;

public class ManageRequestBookActivity extends AppCompatActivity implements AdapterManageRequestBook.ManageRequestBookListener {

    ActivityManageRequestBookBinding binding;
    private final static int REQUEST_CODE = 100003;
    AdapterManageRequestBook adapterManageRequestBook;
    public List<Book> bookRequestUpList;
    Book bookRequestUp;
    public static final long TIME_INTERVAL = 3000;
    long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageRequestBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadRequestUploadBook();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRequestUploadBook();
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
                startActivity(new Intent(ManageRequestBookActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.manageBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageRequestBookActivity.this, ManageBookActivity.class));
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageRequestBookActivity.this, StatisticalActivity.class));
            }
        });
    }

    private void loadRequestUploadBook() {
        bookApi.getAllRequestUploadBook().enqueue(new RetrofitCallBack<GetRequestUploadBookResponse>() {
            @Override
            public void onSuccess(GetRequestUploadBookResponse response) {
                if (response != null){
                    if (response.getCode() == 100){
                        bookRequestUpList = response.getData();
                        loadBookRequestUpBookRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadBookRequestUpBookRecycleView(){
        adapterManageRequestBook = new AdapterManageRequestBook(ManageRequestBookActivity.this, bookRequestUpList);
        adapterManageRequestBook.setManageRequestBookListener(ManageRequestBookActivity.this);
        binding.bookRequestUpRv.setAdapter(adapterManageRequestBook);
    }


    @Override
    public void onItemClick(View view, int position) {
        bookRequestUp = bookRequestUpList.get(position);
        Intent intent = new Intent(view.getContext(), ReactUploadBookActivity.class);
        intent.putExtra("bookRequestUp", bookRequestUp);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                bookRequestUpList.remove(bookRequestUp);
                loadBookRequestUpBookRecycleView();
            }
        }
    }

    private void logout(){
        userApi.logout().enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(ManageRequestBookActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(ManageRequestBookActivity.this, "Quay lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}