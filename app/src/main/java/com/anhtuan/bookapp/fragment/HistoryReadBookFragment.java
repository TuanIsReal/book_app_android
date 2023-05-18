package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterHistoryReadBook;
import com.anhtuan.bookapp.domain.UserBookLibrary;
import com.anhtuan.bookapp.response.GetUserBookLibraryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryReadBookFragment extends Fragment {

    public List<UserBookLibrary> bookList;
    private AdapterHistoryReadBook adapter;
    RecyclerView booksRv;
    SwipeRefreshLayout swipeRefresh;
    String userId;
    View view;

    public HistoryReadBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_read_book, container, false);
        bookList = new ArrayList<>();
        booksRv = view.findViewById(R.id.booksRv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        loadHistoryBook();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHistoryBook();
            }
        });

        return view;
    }

    private void loadHistoryBook(){
        purchasedBookApi.getUserBookLibrary(userId).enqueue(new Callback<GetUserBookLibraryResponse>() {
            @Override
            public void onResponse(Call<GetUserBookLibraryResponse> call, Response<GetUserBookLibraryResponse> response) {
                GetUserBookLibraryResponse responseBody = response.body();
                swipeRefresh.setRefreshing(false);
                if (responseBody.getCode() == 106){
                    Toast.makeText(view.getContext(), "user khong ton tai", Toast.LENGTH_SHORT).show();
                } else if (responseBody.getCode() == 100) {
                    bookList = responseBody.getData();
                    adapter = new AdapterHistoryReadBook(view.getContext(), bookList, userId);
                    booksRv.setAdapter(adapter);
                }else {
                    Toast.makeText(view.getContext(), "Loi khong xac dinh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserBookLibraryResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(view.getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}