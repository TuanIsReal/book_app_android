package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterViewBookInfo;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;

import java.util.ArrayList;

public class ViewBookInfoFragment extends Fragment {

    String bookId;
    String author;
    AdapterViewBookInfo adapterViewBookInfo;
    private ArrayList<Book> books;
    RecyclerView booksRv;
    Book book;
    TextView introductionTv, sameAuthorTv, writeSpeedTv, totalChapterTv, totalPurchasedTv, totalChapterText;
    View view;

    public ViewBookInfoFragment(String bookId) {
        this.bookId = bookId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WindowCompat.setDecorFitsSystemWindows(getActivity().getWindow(), false);
        view = inflater.inflate(R.layout.fragment_view_book_info, container, false);
        booksRv = view.findViewById(R.id.booksRv);
        introductionTv = view.findViewById(R.id.introductionTv);
        sameAuthorTv = view.findViewById(R.id.sameAuthorTv);
        writeSpeedTv = view.findViewById(R.id.writeSpeedTv);
        totalChapterTv = view.findViewById(R.id.totalChapterTv);
        totalPurchasedTv = view.findViewById(R.id.totalPurchasedTv);
        totalChapterText = view.findViewById(R.id.totalChapterText);
        loadBook(bookId);

        return view;
    }

    private void loadBook(String bookId) {
        bookApi.getBookById(bookId).enqueue(new RetrofitCallBack<ViewBookResponse>() {
            @Override
            public void onSuccess(ViewBookResponse response) {
                if (response != null){
                    if (response.getCode() == 109){

                    } else{
                        book = response.getData();
                        loadBookInfo();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadBookInfo() {
        author = book.getAuthor();
        introductionTv.setText(book.getIntroduction());
        totalChapterTv.setText(String.valueOf(book.getTotalChapter()));
        totalPurchasedTv.setText(String.valueOf(book.getTotalPurchased()));
        if (book.getStatus() == 2){
            totalChapterText.setText("Chương-Hoàn thành");
        }
        writeSpeedTv.setText(getWriteSpeed(book.getTotalChapter(), book.getUploadTime()));
        userApi.getUsername(book.getAuthor()).enqueue(new RetrofitCallBack<GetUsernameResponse>() {
            @Override
            public void onSuccess(GetUsernameResponse response) {
                if (response != null && response.getCode() == 100){
                    sameAuthorTv.setText("Cùng đăng bởi " + response.getData());
                    loadBookSameAuthor(author, bookId);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadBookSameAuthor(String author, String bookId) {
        bookApi.getBookByAuthor(author, bookId).enqueue(new RetrofitCallBack<SearchBookResponse>() {
            @Override
            public void onSuccess(SearchBookResponse response) {
                if (response.getCode() == 100){
                    books = response.getData();
                    adapterViewBookInfo = new AdapterViewBookInfo(view.getContext(), books);
                    booksRv.setAdapter(adapterViewBookInfo);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private String getWriteSpeed(int totalChapter, long uploadTime){
        long currentTime = System.currentTimeMillis();
        long timeUpBook = currentTime - uploadTime;
        int speed;
        if (timeUpBook <= Constant.A_DAY * 7){
            speed = totalChapter;
        } else {
            speed = (int) ((totalChapter * Constant.A_DAY * 7) / timeUpBook);
        }

        return String.valueOf(speed);
    }
}