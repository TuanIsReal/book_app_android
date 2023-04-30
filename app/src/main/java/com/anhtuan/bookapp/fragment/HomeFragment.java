package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.DashboardUserActivity;
import com.anhtuan.bookapp.activity.ListBookFilterActivity;
import com.anhtuan.bookapp.activity.SearchBookUserActivity;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.adapter.AdapterMostBuyBook;
import com.anhtuan.bookapp.adapter.AdapterMostReviewBook;
import com.anhtuan.bookapp.adapter.AdapterNewBook;
import com.anhtuan.bookapp.adapter.AdapterRecommendBook;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements AdapterNewBook.NewBookListener {

    ImageButton searchBookBtn, filterBookBtn;
    List<Book> newBookList, recommendBookList, mostBuyBookList, mostReviewBook;
    RecyclerView newBookRv, recommendBooksRv, mostBuyRv, mostReviewRv;
    AdapterNewBook adapterNewBook;
    TextView categoryNewTv, nameNewTv, introductionNewTv, ratingPointNew, newBookBt;
    TextView recommendTv, mostBuyTv, mostReviewTv, newBookTv;
    RatingBar ratingBarNew;
    ImageView imageBookNew;
    View view;
    Book newBook;
    AdapterRecommendBook adapterRecommendBook;
    AdapterMostBuyBook adapterMostBuyBook;
    AdapterMostReviewBook adapterMostReviewBook;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        newBook = new Book();
        loadNewBook();



        searchBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), SearchBookUserActivity.class));
            }
        });


        newBookBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ViewBookActivity.class);
                intent.putExtra("bookId", newBook.getId());
                view.getContext().startActivity(intent);
            }
        });

        filterBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListBookFilterActivity.class);
                intent.putExtra("filterType", 1);
                startActivity(intent);
            }
        });

        newBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListBookFilterActivity.class);
                intent.putExtra("filterType", 1);
                startActivity(intent);
            }
        });

        recommendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListBookFilterActivity.class);
                intent.putExtra("filterType", 2);
                startActivity(intent);
            }
        });

        mostBuyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListBookFilterActivity.class);
                intent.putExtra("filterType", 3);
                startActivity(intent);
            }
        });

        mostReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ListBookFilterActivity.class);
                intent.putExtra("filterType", 4);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView() {
        searchBookBtn = view.findViewById(R.id.searchBookBtn);
        newBookRv = view.findViewById(R.id.newBookRv);
        categoryNewTv = view.findViewById(R.id.categoryNewTv);
        nameNewTv = view.findViewById(R.id.nameNewTv);
        introductionNewTv = view.findViewById(R.id.introductionNewTv);
        ratingPointNew = view.findViewById(R.id.ratingPointNew);
        ratingBarNew = view.findViewById(R.id.ratingBarNew);
        imageBookNew = view.findViewById(R.id.imageBookNew);
        newBookBt = view.findViewById(R.id.newBookBt);
        recommendBooksRv = view.findViewById(R.id.recommendBooksRv);
        mostBuyRv = view.findViewById(R.id.mostBuyRv);
        mostReviewRv = view.findViewById(R.id.mostReviewRv);
        filterBookBtn = view.findViewById(R.id.filterBookBtn);
        newBookTv = view.findViewById(R.id.newBookTv);
        recommendTv = view.findViewById(R.id.recommendTv);
        mostBuyTv = view.findViewById(R.id.mostBuyTv);
        mostReviewTv = view.findViewById(R.id.mostReviewTv);
    }

    private void loadNewBook() {

        bookApi.getBookHome(Constant.TYPE_FILTER.NEW_BOOK, true).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    newBookList = response.body().getData();
                    if (newBookList.size() > 0){
                        setNewBookInfo(newBookList.get(0));
                    }
                    adapterNewBook = new AdapterNewBook(view.getContext(), newBookList);
                    newBookRv.setAdapter(adapterNewBook);
                    adapterNewBook.setNewBookListener(HomeFragment.this);


                    loadRecommendBook();
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Book book = newBookList.get(position);
        setNewBookInfo(book);
    }

    private void setNewBookInfo(Book book){
        newBook = book;
        nameNewTv.setText(book.getBookName());
        introductionNewTv.setText(book.getIntroduction());
        categoryNewTv.setText(Utils.toStringCategory(book.getBookCategory()));
        ratingPointNew.setText(String.valueOf(book.getStar()));
        ratingBarNew.setRating((float) book.getStar());

        if (book.getBookImage() != null && !book.getBookImage().isBlank()){
            bookApi.getBookImage(book.getBookImage()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        try {
                            byte[] bytes = response.body().bytes();
                            Glide.with(view.getContext())
                                    .load(bytes)
                                    .into(imageBookNew);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            imageBookNew.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.logo));
        }
    }

    private void loadRecommendBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.RECOMMEND_BOOK, true).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    recommendBookList = response.body().getData();
                    adapterRecommendBook = new AdapterRecommendBook(view.getContext(), recommendBookList);
                    recommendBooksRv.setAdapter(adapterRecommendBook);
                    loadMostBuyBook();

                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });
    }

    private void loadMostBuyBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.MOST_BUY, true).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    mostBuyBookList = response.body().getData();
                    adapterMostBuyBook = new AdapterMostBuyBook(view.getContext(), mostBuyBookList);
                    mostBuyRv.setAdapter(adapterMostBuyBook);
                    loadMostReviewBook();
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });
    }

    private void loadMostReviewBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.MOST_REVIEW, true).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    mostReviewBook = response.body().getData();
                    adapterMostReviewBook = new AdapterMostReviewBook(view.getContext(), mostReviewBook);
                    mostReviewRv.setAdapter(adapterMostReviewBook);

                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });
    }
}