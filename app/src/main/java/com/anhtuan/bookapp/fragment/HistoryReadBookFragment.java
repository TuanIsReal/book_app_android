package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterHistoryReadBook;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.domain.UserBookLibrary;
import com.anhtuan.bookapp.response.GetUserBookLibraryResponse;

import java.util.ArrayList;
import java.util.List;

public class HistoryReadBookFragment extends Fragment {

    public List<UserBookLibrary> bookList;
    private AdapterHistoryReadBook adapter;
    RecyclerView booksRv;
    SwipeRefreshLayout swipeRefresh;
    View view;

    public HistoryReadBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_history_read_book, container, false);
        bookList = new ArrayList<>();
        booksRv = view.findViewById(R.id.booksRv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
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
        purchasedBookApi.getUserBookLibrary().enqueue(new RetrofitCallBack<GetUserBookLibraryResponse>() {
            @Override
            public void onSuccess(GetUserBookLibraryResponse response) {
                swipeRefresh.setRefreshing(false);
                if (response.getCode() == 106){
                    Toast.makeText(view.getContext(), "user khong ton tai", Toast.LENGTH_SHORT).show();
                } else if (response.getCode() == 100) {
                    bookList = response.getData();
                    adapter = new AdapterHistoryReadBook(bookList);
                    booksRv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}