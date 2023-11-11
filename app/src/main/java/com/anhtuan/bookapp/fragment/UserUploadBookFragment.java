package com.anhtuan.bookapp.fragment;

import static android.app.Activity.RESULT_OK;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.BookAddActivity;
import com.anhtuan.bookapp.adapter.AdapterRejectUploadBook;
import com.anhtuan.bookapp.adapter.AdapterRequestUploadBook;
import com.anhtuan.bookapp.adapter.AdapterSuccessUploadBook;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetRequestUploadBookResponse;

import java.util.ArrayList;
import java.util.List;

public class UserUploadBookFragment extends Fragment {

    private final static int REQUEST_CODE = 100002;
    private View view;
    private AdapterRequestUploadBook adapterRequestUploadBook;
    private AdapterSuccessUploadBook adapterSuccessUploadBook;
    private AdapterRejectUploadBook adapterRejectUploadBook;
    Button addBookBtn;
    RecyclerView successBooksRv, requestBooksRv, rejectBooksRv;
    List<Book> bookRequestList, bookRejectList;
    List<Book> bookSuccessList;
    TextView requestUploadTv, successUploadTv, rejectUploadTv;

    public UserUploadBookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_user_upload_book, container, false);
        initView();

        successUploadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successBooksRv.getVisibility() == View.GONE){
                    loadSuccessUploadBook();
                    successBooksRv.setVisibility(View.VISIBLE);
                } else if (successBooksRv.getVisibility() == View.VISIBLE){
                    successBooksRv.setVisibility(View.GONE);
                }
            }
        });

        requestUploadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestBooksRv.getVisibility() == View.GONE){
                    loadRequestUploadBook();
                    requestBooksRv.setVisibility(View.VISIBLE);
                } else if (requestBooksRv.getVisibility() == View.VISIBLE){
                    requestBooksRv.setVisibility(View.GONE);
                }
            }
        });

        rejectUploadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rejectBooksRv.getVisibility() == View.GONE){
                    loadRejectUploadBook();
                    rejectBooksRv.setVisibility(View.VISIBLE);
                } else if (rejectBooksRv.getVisibility() == View.VISIBLE){
                    rejectBooksRv.setVisibility(View.GONE);
                }
            }
        });

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), BookAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    private void initView() {
        successBooksRv = view.findViewById(R.id.successBooksRv);
        requestBooksRv = view.findViewById(R.id.requestBooksRv);
        rejectBooksRv = view.findViewById(R.id.rejectBooksRv);
        addBookBtn = view.findViewById(R.id.addBookBtn);
        requestUploadTv = view.findViewById(R.id.requestUploadTv);
        successUploadTv = view.findViewById(R.id.successUploadTv);
        rejectUploadTv = view.findViewById(R.id.rejectUploadTv);
        successBooksRv.setVisibility(View.GONE);
        requestBooksRv.setVisibility(View.GONE);
        rejectBooksRv.setVisibility(View.GONE);
    }

    private void loadRequestUploadBook(){
        bookRequestList = new ArrayList<>();
        bookApi.getRequestUploadBook(Constant.StatusBookRequestUp.REQUEST).enqueue(new RetrofitCallBack<GetRequestUploadBookResponse>() {
            @Override
            public void onSuccess(GetRequestUploadBookResponse response) {
                if (response != null){
                    if (response.getCode() == 100){
                        bookRequestList = response.getData();
                        adapterRequestUploadBook = new AdapterRequestUploadBook(view.getContext(), bookRequestList);
                        requestBooksRv.setAdapter(adapterRequestUploadBook);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadSuccessUploadBook(){
        bookSuccessList = new ArrayList<>();
        bookApi.getRequestUploadBook(Constant.StatusBookRequestUp.ACCEPTED).enqueue(new RetrofitCallBack<GetRequestUploadBookResponse>() {
            @Override
            public void onSuccess(GetRequestUploadBookResponse response) {
                if (response != null){
                    if (response.getCode() == 100){
                        bookSuccessList = response.getData();
                        adapterSuccessUploadBook = new AdapterSuccessUploadBook(view.getContext(), bookSuccessList);
                        successBooksRv.setAdapter(adapterSuccessUploadBook);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadRejectUploadBook(){
        bookRejectList = new ArrayList<>();
        bookApi.getRequestUploadBook(Constant.StatusBookRequestUp.REJECTED).enqueue(new RetrofitCallBack<GetRequestUploadBookResponse>() {
            @Override
            public void onSuccess(GetRequestUploadBookResponse response) {
                if (response != null){
                    if (response.getCode() == 100){
                        bookRejectList = response.getData();
                        adapterRejectUploadBook = new AdapterRejectUploadBook(view.getContext(), bookRejectList);
                        rejectBooksRv.setAdapter(adapterRejectUploadBook);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            loadRequestUploadBook();
        }
    }
}