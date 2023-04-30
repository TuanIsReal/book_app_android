package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

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
import com.anhtuan.bookapp.adapter.AdapterViewBookInfo;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.SearchBookResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookInfoFragment extends Fragment {

    String bookId;
    String introduction;
    String author;
    AdapterViewBookInfo adapterViewBookInfo;
    private ArrayList<Book> books;
    RecyclerView booksRv;
    TextView introductionTv, sameAuthorTv;
    View view;

    public ViewBookInfoFragment(String bookId, String introduction, String author) {
        this.bookId = bookId;
        this.introduction = introduction;
        this.author = author;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_book_info, container, false);
        booksRv = view.findViewById(R.id.booksRv);
        introductionTv = view.findViewById(R.id.introductionTv);
        sameAuthorTv = view.findViewById(R.id.sameAuthorTv);
        introductionTv.setText(introduction);
        sameAuthorTv.setText("Cùng đăng bởi " + author);
        loadBookSameAuthor(author, bookId);
        return view;
    }

    private void loadBookSameAuthor(String author, String bookId) {
        bookApi.getBookByAuthor(author, bookId).enqueue(new Callback<SearchBookResponse>() {
            @Override
            public void onResponse(Call<SearchBookResponse> call, Response<SearchBookResponse> response) {
                SearchBookResponse searchBookResponse = response.body();
                if (searchBookResponse.getCode() == 100){
                    books = searchBookResponse.getData();
                    adapterViewBookInfo = new AdapterViewBookInfo(view.getContext(), books);
                    booksRv.setAdapter(adapterViewBookInfo);
                }else {
                    Toast.makeText(view.getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchBookResponse> call, Throwable t) {
                Toast.makeText(view.getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}