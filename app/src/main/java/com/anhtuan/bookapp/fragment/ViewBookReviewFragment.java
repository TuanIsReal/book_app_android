package com.anhtuan.bookapp.fragment;

import static android.app.Activity.RESULT_OK;
import static com.anhtuan.bookapp.api.BookReviewApi.bookReviewApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.NewBookReviewActivity;
import com.anhtuan.bookapp.activity.ViewChapterActivity;
import com.anhtuan.bookapp.adapter.AdapterViewBookReview;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.domain.BookReview;
import com.anhtuan.bookapp.response.CheckPurchasedBookResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetBookReviewResponse;
import com.anhtuan.bookapp.response.GetBookReviewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookReviewFragment extends Fragment {

    public static final int REQUEST_CODE = 10005;
    String bookId;
    View view;
    TextView numberRatingTv, newReviewTv;
    RecyclerView bookReviewsRv;
    AdapterViewBookReview adapterViewBookReview;
    List<BookReview> bookReviewList;
    boolean isPurchased;
    String author;

    public ViewBookReviewFragment(String bookId, boolean isPurchased, String author) {
        this.bookId = bookId;
        this.isPurchased = isPurchased;
        this.author = author;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_view_book_review, container, false);
        numberRatingTv = view.findViewById(R.id.numberRatingTv);
        newReviewTv = view.findViewById(R.id.newReviewTv);
        bookReviewsRv = view.findViewById(R.id.bookReviewsRv);
        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 100){
                    if (!isPurchased || response.getData().getId().equals(author)){
                        newReviewTv.setVisibility(View.GONE);
                        loadBookReviews();
                    } else {
                        checkExistBookReview();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                newReviewTv.setVisibility(View.GONE);
                loadBookReviews();
            }
        });

        newReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedBookApi.checkPurchasedBook(bookId).enqueue(new RetrofitCallBack<CheckPurchasedBookResponse>() {
                    @Override
                    public void onSuccess(CheckPurchasedBookResponse response) {
                        if (response != null && response.getCode() == 100){
                            if (response.getData() == 1){
                                Intent intent = new Intent(view.getContext(), NewBookReviewActivity.class);
                                intent.putExtra("bookId", bookId);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                    }
                });
            }
        });

        return view;
    }

    private void loadBookReviews(){
        bookReviewApi.getBookReviews(bookId).enqueue(new RetrofitCallBack<GetBookReviewsResponse>() {
            @Override
            public void onSuccess(GetBookReviewsResponse response) {
                if (response != null && response.getCode() == 100){
                    bookReviewList = response.getData();
                    numberRatingTv.setText("(" + bookReviewList.size() + ")");
                    adapterViewBookReview = new AdapterViewBookReview(view.getContext(), bookReviewList);
                    bookReviewsRv.setAdapter(adapterViewBookReview);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void checkExistBookReview(){
        bookReviewApi.getBookReview(bookId).enqueue(new RetrofitCallBack<GetBookReviewResponse>() {
            @Override
            public void onSuccess(GetBookReviewResponse response) {
                if (response != null && response.getCode() != 119){
                    newReviewTv.setVisibility(View.GONE);
                }

                loadBookReviews();
            }

            @Override
            public void onFailure(String errorMessage) {
                newReviewTv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            newReviewTv.setVisibility(View.GONE);
            loadBookReviews();
        }
    }
}