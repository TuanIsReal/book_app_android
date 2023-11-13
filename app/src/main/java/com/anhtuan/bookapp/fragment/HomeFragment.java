package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.ListBookFilterActivity;
import com.anhtuan.bookapp.activity.MainActivity;
import com.anhtuan.bookapp.activity.SearchBookUserActivity;
import com.anhtuan.bookapp.activity.SplashActivity;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.adapter.AdapterMostBuyBook;
import com.anhtuan.bookapp.adapter.AdapterMostReviewBook;
import com.anhtuan.bookapp.adapter.AdapterNewBook;
import com.anhtuan.bookapp.adapter.AdapterRecommendBook;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

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
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 122 || response.getCode() == 106){
                    AccountManager.getInstance().logoutAccount();
                    TokenManager.getInstance().deleteToken();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });

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
        bookApi.getBookHome(Constant.TYPE_FILTER.NEW_BOOK, 8).enqueue(new RetrofitCallBack<GetBookResponse>() {
            @Override
            public void onSuccess(GetBookResponse response) {
                if (response != null && response.getCode() == 100){
                    newBookList = response.getData();
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
            public void onFailure(String errorMessage) {

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
            unAuthApi.getThumbnail(book.getBookImage()).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().getCode() == 100){
                        String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                        Glide.with(view.getContext())
                                .load(path)
                                .signature(new ObjectKey(path))
                                .into(imageBookNew);
                    }
                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {

                }
            });
        } else {
            imageBookNew.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.logo));
        }
    }

    private void loadRecommendBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.RECOMMEND_BOOK, 6).enqueue(new RetrofitCallBack<GetBookResponse>() {
            @Override
            public void onSuccess(GetBookResponse response) {
                if (response != null && response.getCode() == 100){
                    recommendBookList = response.getData();
                    adapterRecommendBook = new AdapterRecommendBook(view.getContext(), recommendBookList);
                    recommendBooksRv.setAdapter(adapterRecommendBook);
                    loadMostBuyBook();

                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadMostBuyBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.MOST_BUY, 6).enqueue(new RetrofitCallBack<GetBookResponse>() {
            @Override
            public void onSuccess(GetBookResponse response) {
                if (response != null && response.getCode() == 100){
                    mostBuyBookList = response.getData();
                    adapterMostBuyBook = new AdapterMostBuyBook(view.getContext(), mostBuyBookList);
                    mostBuyRv.setAdapter(adapterMostBuyBook);
                    loadMostReviewBook();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadMostReviewBook(){
        bookApi.getBookHome(Constant.TYPE_FILTER.MOST_REVIEW, 6).enqueue(new RetrofitCallBack<GetBookResponse>() {
            @Override
            public void onSuccess(GetBookResponse response) {
                if (response != null && response.getCode() == 100){
                    mostReviewBook = response.getData();
                    adapterMostReviewBook = new AdapterMostReviewBook(view.getContext(), mostReviewBook);
                    mostReviewRv.setAdapter(adapterMostReviewBook);

                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}