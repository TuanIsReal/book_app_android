package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookRequestUpApi.bookRequestUpApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterManageRequestBook;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityManageRequestBookBinding;
import com.anhtuan.bookapp.domain.BookRequestUp;
import com.anhtuan.bookapp.response.GetRequestUploadBookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRequestBookActivity extends AppCompatActivity implements AdapterManageRequestBook.ManageRequestBookListener {

    ActivityManageRequestBookBinding binding;
    private final static int REQUEST_CODE = 100003;
    AdapterManageRequestBook adapterManageRequestBook;
    public List<BookRequestUp> bookRequestUpList;
    BookRequestUp bookRequestUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageRequestBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadRequestUploadBook();
    }

    private void loadRequestUploadBook() {
        bookRequestUpApi.getAllRequestUploadBook().enqueue(new Callback<GetRequestUploadBookResponse>() {
            @Override
            public void onResponse(Call<GetRequestUploadBookResponse> call, Response<GetRequestUploadBookResponse> response) {
                if (response.body() != null){
                    GetRequestUploadBookResponse responseBody = response.body();
                    if (responseBody.getCode() == 100){
                        bookRequestUpList = responseBody.getData();
                        loadBookRequestUpBookRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRequestUploadBookResponse> call, Throwable t) {

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
}