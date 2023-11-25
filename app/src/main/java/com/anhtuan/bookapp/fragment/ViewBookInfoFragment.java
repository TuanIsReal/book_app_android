package com.anhtuan.bookapp.fragment;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.ReportApi.reportApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.app.Dialog;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterViewBookInfo;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.request.AddReportRequest;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
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
    TextView introductionTv, sameAuthorTv, writeSpeedTv, totalChapterTv, totalPurchasedTv, totalChapterText, reportTv;
    View view;
    Button buttonSubmit;
    EditText reportContentEt;
    RadioGroup radioGroupPurpose;

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
        reportTv = view.findViewById(R.id.reportTv);
        loadBook(bookId);

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportDialog();
            }
        });

        return view;
    }

    private void openReportDialog() {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_warning);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        radioGroupPurpose = dialog.findViewById(R.id.radioGroupPurpose);
        reportContentEt = dialog.findViewById(R.id.reportContentEt);
        buttonSubmit = dialog.findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int purpose;

                switch (radioGroupPurpose.getCheckedRadioButtonId()){
                    case R.id.adultContent:
                        purpose = 1;
                        break;
                    case R.id.hatred:
                        purpose = 2;
                        break;
                    case R.id.personalAttack:
                        purpose = 3;
                        break;
                    case R.id.spam:
                        purpose = 4;
                        break;
                    case R.id.other:
                        purpose = 5;
                        break;
                    default:
                        purpose = 1;
                        break;
                }

                reportApi.addReport(new AddReportRequest(bookId, purpose, reportContentEt.getText().toString())).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response != null && response.getCode() == 100){
                            dialog.dismiss();
                            Toast.makeText(view.getContext(), "Đã báo lỗi lên hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        });

        dialog.show();
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
            if (speed == 0 && totalChapter > 0){
                speed = 1;
            }
        }

        return String.valueOf(speed);
    }
}