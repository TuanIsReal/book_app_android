package com.anhtuan.bookapp.fragment;

import static android.app.Activity.RESULT_OK;
import static com.anhtuan.bookapp.api.BookReviewApi.bookReviewApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.NewBookReviewActivity;
import com.anhtuan.bookapp.activity.ViewChapterActivity;
import com.anhtuan.bookapp.adapter.AdapterViewBookReview;
import com.anhtuan.bookapp.domain.BookReview;
import com.anhtuan.bookapp.response.CheckPurchasedBookResponse;
import com.anhtuan.bookapp.response.GetBookReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookReviewFragment extends Fragment {

    public static final int REQUEST_CODE = 10005;
    String bookId;
    View view;
    String userId;
    TextView numberRatingTv, newReviewTv;
    RecyclerView bookReviewsRv;
    AdapterViewBookReview adapterViewBookReview;
    List<BookReview> bookReviewList;
    boolean isPurchased;

    public ViewBookReviewFragment(String bookId, boolean isPurchased) {
        this.bookId = bookId;
        this.isPurchased = isPurchased;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_book_review, container, false);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        numberRatingTv = view.findViewById(R.id.numberRatingTv);
        newReviewTv = view.findViewById(R.id.newReviewTv);
        bookReviewsRv = view.findViewById(R.id.bookReviewsRv);

        if (!isPurchased){
            newReviewTv.setVisibility(View.GONE);
            loadBookReviews();
        } else {
            checkExistBookReview();
        }


        newReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedBookApi.checkPurchasedBook(bookId, userId).enqueue(new Callback<CheckPurchasedBookResponse>() {
                    @Override
                    public void onResponse(Call<CheckPurchasedBookResponse> call, Response<CheckPurchasedBookResponse> response) {
                        if (response.body() != null && response.body().getCode() == 100){
                            if (response.body().getData() == 1){
                                Intent intent = new Intent(view.getContext(), NewBookReviewActivity.class);
                                intent.putExtra("bookId", bookId);
                                intent.putExtra("userId", userId);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckPurchasedBookResponse> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    private void loadBookReviews(){
        bookReviewApi.getBookReviews(bookId).enqueue(new Callback<GetBookReviewResponse>() {
            @Override
            public void onResponse(Call<GetBookReviewResponse> call, Response<GetBookReviewResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    bookReviewList = response.body().getData();
                    numberRatingTv.setText("(" + bookReviewList.size() + ")");
                    adapterViewBookReview = new AdapterViewBookReview(view.getContext(), bookReviewList);
                    bookReviewsRv.setAdapter(adapterViewBookReview);
                }
            }

            @Override
            public void onFailure(Call<GetBookReviewResponse> call, Throwable t) {

            }
        });
    }

    private void checkExistBookReview(){
        bookReviewApi.getBookReview(bookId, userId).enqueue(new Callback<GetBookReviewResponse>() {
            @Override
            public void onResponse(Call<GetBookReviewResponse> call, Response<GetBookReviewResponse> response) {
                if (response.body() != null && response.body().getCode() != 119){
                    newReviewTv.setVisibility(View.GONE);
                }

                loadBookReviews();
            }

            @Override
            public void onFailure(Call<GetBookReviewResponse> call, Throwable t) {
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