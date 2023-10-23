package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityViewChapterBinding;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.response.GetChapterContentResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewChapterActivity extends AppCompatActivity {
    ActivityViewChapterBinding binding;

    int chapterNumber;
    String bookId;
    String userId;
    boolean showBottomToolbar;
    String theme;
    public static final int REQUEST_CODE = 10006;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityViewChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("userId","");
        theme = sharedPreferences.getString("theme", "light");

        changeTheme(theme);

        if (theme.equals("dark")){
            binding.darkThemeTv.setVisibility(View.GONE);
        } else {
            binding.lightThemeTv.setVisibility(View.GONE);
        }

        showBottomToolbar = false;
        binding.bottomToolbarRl.setVisibility(View.GONE);


        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        chapterNumber = intent.getIntExtra("chapterNumber",1);
        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadContent();

        binding.bottomToolbarRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomToolbar = true;
            }
        });

        binding.bodyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showBottomToolbar){
                    actionClick();
                } else {
                    showBottomToolbar = false;
                }
            }
        });

        binding.darkThemeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme("dark");
                binding.darkThemeTv.setVisibility(View.GONE);
                editor.putString("theme", "dark");
                editor.apply();
                binding.lightThemeTv.setVisibility(View.VISIBLE);
            }
        });

        binding.lightThemeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme("light");
                binding.lightThemeTv.setVisibility(View.GONE);
                editor.putString("theme", "light");
                editor.apply();
                binding.darkThemeTv.setVisibility(View.VISIBLE);
            }
        });


        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1).getBottom()
                        == (binding.scrollView.getHeight() + binding.scrollView.getScrollY())) {
                    nextChapter();
                }
            }
        });

        binding.afterChapterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextChapter();
            }
        });

        binding.beforeChapterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beforeChapter();
            }
        });

        binding.bookInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComment();
            }
        });

        binding.listChapterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewChapterActivity.this, ViewChapterListActivity.class);
                intent1.putExtra("bookId", bookId);
                intent1.putExtra("userId", userId);
                startActivityForResult(intent1, REQUEST_CODE);
            }
        });

    }

    private void actionClick(){
        if (binding.bottomToolbarRl.getVisibility() == View.GONE){
            binding.bottomToolbarRl.setVisibility(View.VISIBLE);
        } else {
            binding.bottomToolbarRl.setVisibility(View.GONE);
        }
    }

    private void loadContent() {
        bookChapterApi.getChapterContent(userId, bookId, chapterNumber).enqueue(new Callback<GetChapterContentResponse>() {
            @Override
            public void onResponse(Call<GetChapterContentResponse> call, Response<GetChapterContentResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null){
                    GetChapterContentResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){
                        binding.contentTv.setText("Không tìm thấy sách" + "\n \n \n \n \n \n \n \n \n \n" );
                    } else if (responseBody.getCode() == 100) {
                        BookChapter bookChapter = responseBody.getData();
                        chapterNumber = bookChapter.getChapterNumber();
                        binding.charterTv.setText("Chương " + bookChapter.getChapterNumber() + ": " + bookChapter.getChapterName());
                        binding.contentTv.setText("\n" + bookChapter.getChapterContent()+ "\n \n \n \n \n \n \n \n \n \n" );
                    } else if (responseBody.getCode() == 113) {
                        openComment();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChapterContentResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewChapterActivity.this,""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeTheme(String theme){
        if (theme.equals("dark")){
            binding.scrollView.setBackgroundColor(getResources().getColor(R.color.black));
            binding.toolbarChapterRl.setBackgroundColor(getResources().getColor(R.color.black));
            binding.contentTv.setTextColor(getResources().getColor(R.color.white));
            binding.charterTv.setTextColor(getResources().getColor(R.color.white));
            binding.returnBtn.setImageDrawable(getDrawable(R.drawable.ic_left_white));
        } else {
            binding.scrollView.setBackgroundColor(getResources().getColor(R.color.gray04));
            binding.toolbarChapterRl.setBackgroundColor(getResources().getColor(R.color.gray04));
            binding.contentTv.setTextColor(getResources().getColor(R.color.black));
            binding.charterTv.setTextColor(getResources().getColor(R.color.black));
            binding.returnBtn.setImageDrawable(getDrawable(R.drawable.ic_left_black));
        }
    }

    private void openComment(){
        Intent intent = new Intent(ViewChapterActivity.this, ViewCommentActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void nextChapter(){
        chapterNumber += 1;
        bookChapterApi.getChapterContent(userId, bookId, chapterNumber).enqueue(new Callback<GetChapterContentResponse>() {
            @Override
            public void onResponse(Call<GetChapterContentResponse> call, Response<GetChapterContentResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null){
                    GetChapterContentResponse responseBody = response.body();
                    if (responseBody.getCode() == 113) {
                        openComment();
                    } else {
                        Intent intent = new Intent(ViewChapterActivity.this, ViewChapterActivity.class);
                        intent.putExtra("bookId", bookId);
                        intent.putExtra("chapterNumber", chapterNumber);
                        finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChapterContentResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewChapterActivity.this,""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void beforeChapter(){
        chapterNumber -= 1;
        Intent intent = new Intent(ViewChapterActivity.this, ViewChapterActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("chapterNumber", chapterNumber);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            int chapterNum = data.getIntExtra("chapterNumber", 1);
            chapterNumber = chapterNum - 1;
            nextChapter();
        }
    }
}