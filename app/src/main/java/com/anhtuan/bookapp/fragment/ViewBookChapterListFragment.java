package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.activity.ViewChapterActivity;
import com.anhtuan.bookapp.adapter.AdapterBookChapterList;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.PurchasedBook;
import com.anhtuan.bookapp.response.GetBookChapterListResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;

import java.util.List;


public class ViewBookChapterListFragment extends Fragment implements AdapterBookChapterList.BookChapterItemListener{

    String bookId;
    TextView chapterNum;
    RecyclerView bookChaptersRv;
    List<BookChapter> bookChapters;
    AdapterBookChapterList adapterBookChapterList;
    View view;
    int lastReadChapter;
    SwipeRefreshLayout swipeRefresh;

    public ViewBookChapterListFragment(String bookId) {
        this.bookId = bookId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_view_book_chapter_list, container, false);
        chapterNum = view.findViewById(R.id.chapterNum);
        bookChaptersRv = view.findViewById(R.id.bookChaptersRv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        loadPurchasedBook();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPurchasedBook();
            }
        });
        return view;
    }

    private void loadPurchasedBook(){
        purchasedBookApi.getPurchasedBook(bookId).enqueue(new RetrofitCallBack<GetPurchasedBookResponse>() {
            @Override
            public void onSuccess(GetPurchasedBookResponse response) {
                if (response != null){
                    if (response.getCode() == 106){
                        Toast.makeText(view.getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 120) {
                        loadBookChapterList();
                    } else if (response.getCode() == 100) {
                        PurchasedBook purchasedBook = response.getData();
                        lastReadChapter = purchasedBook.getLastReadChapter();
                        loadBookChapterList();
                    }
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void loadBookChapterList(){
        bookChapterApi.getBookChapterList(bookId).enqueue(new RetrofitCallBack<GetBookChapterListResponse>() {
            @Override
            public void onSuccess(GetBookChapterListResponse response) {
                if (response != null){
                    if (response.getCode() == 106){

                    } else if (response.getCode() == 100) {
                        bookChapters = response.getData();
                        chapterNum.setText("(" + bookChapters.size() +")");
                        adapterBookChapterList = new AdapterBookChapterList(view.getContext(), bookChapters, lastReadChapter);
                        bookChaptersRv.setAdapter(adapterBookChapterList);
                        adapterBookChapterList.setBookChapterItemListener(ViewBookChapterListFragment.this);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        int chapterNumber = bookChapters.get(position).getChapterNumber();
        Intent intent = new Intent(view.getContext(), ViewChapterActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("chapterNumber", chapterNumber);
        startActivity(intent);
    }
}