package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.activity.ViewChapterActivity;
import com.anhtuan.bookapp.adapter.AdapterBookChapterList;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.PurchasedBook;
import com.anhtuan.bookapp.response.CheckPurchasedBookResponse;
import com.anhtuan.bookapp.response.GetBookChapterListResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookChapterListFragment extends Fragment implements AdapterBookChapterList.BookChapterItemListener{

    String bookId;
    TextView chapterNum;
    RecyclerView bookChaptersRv;
    List<BookChapter> bookChapters;
    AdapterBookChapterList adapterBookChapterList;
    String userId;
    View view;
    int lastReadChapter;
    public ViewBookChapterListFragment(String bookId, String userId) {
        this.bookId = bookId;
        this.userId = userId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_book_chapter_list, container, false);
        chapterNum = view.findViewById(R.id.chapterNum);
        bookChaptersRv = view.findViewById(R.id.bookChaptersRv);

        loadPurchasedBook();

        return view;
    }

    private void loadPurchasedBook(){
        purchasedBookApi.getPurchasedBook(bookId, userId).enqueue(new Callback<GetPurchasedBookResponse>() {
            @Override
            public void onResponse(Call<GetPurchasedBookResponse> call, Response<GetPurchasedBookResponse> response) {
                if (response != null){
                    GetPurchasedBookResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){
                        Toast.makeText(view.getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 112) {
                        loadBookChapterList();
                    } else if (responseBody.getCode() == 100) {
                        PurchasedBook purchasedBook = responseBody.getData();
                        lastReadChapter = purchasedBook.getLastReadChapter();
                        loadBookChapterList();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPurchasedBookResponse> call, Throwable t) {
                Toast.makeText(view.getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookChapterList(){
        bookChapterApi.getBookChapterList(bookId).enqueue(new Callback<GetBookChapterListResponse>() {
            @Override
            public void onResponse(Call<GetBookChapterListResponse> call, Response<GetBookChapterListResponse> response) {
                if (response.body() != null){
                    GetBookChapterListResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){

                    } else if (responseBody.getCode() == 100) {
                        bookChapters = responseBody.getData();
                        chapterNum.setText("(" + bookChapters.size() +")");
                        adapterBookChapterList = new AdapterBookChapterList(view.getContext(), bookChapters, lastReadChapter);
                        bookChaptersRv.setAdapter(adapterBookChapterList);
                        adapterBookChapterList.setBookChapterItemListener(ViewBookChapterListFragment.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookChapterListResponse> call, Throwable t) {
                Toast.makeText(view.getContext(), "" +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        purchasedBookApi.checkPurchasedBook(bookId, userId).enqueue(new Callback<CheckPurchasedBookResponse>() {
            @Override
            public void onResponse(Call<CheckPurchasedBookResponse> call, Response<CheckPurchasedBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    if (response.body().getData() == 1){
                        int chapterNumber = bookChapters.get(position).getChapterNumber();
                        Intent intent = new Intent(view.getContext(), ViewChapterActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("bookId", bookId);
                        intent.putExtra("chapterNumber", chapterNumber);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckPurchasedBookResponse> call, Throwable t) {

            }
        });
    }
}