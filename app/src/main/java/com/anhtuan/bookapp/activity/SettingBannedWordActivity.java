package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterBannedWord;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivitySettingBannedWordBinding;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;

public class SettingBannedWordActivity extends AppCompatActivity {

    ActivitySettingBannedWordBinding binding;
    AdapterBannedWord adapterBannedWord;
    List<String> bannedWords;
    EditText bannedWordEt;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivitySettingBannedWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBannedWordList();

        binding.addBannedWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddBannedWord();
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void openDialogAddBannedWord() {
        final Dialog dialog = new Dialog(SettingBannedWordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_banned_word);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        bannedWordEt = dialog.findViewById(R.id.bannedWordEt);
        buttonSubmit = dialog.findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bannedWord = bannedWordEt.getText().toString();
                if (bannedWord.isEmpty()){
                    Toast.makeText(SettingBannedWordActivity.this, "Chưa nhập từ cấm", Toast.LENGTH_SHORT).show();
                } else if (bannedWord.contains(" ")) {
                    Toast.makeText(SettingBannedWordActivity.this, "Chỉ nhập 1 từ", Toast.LENGTH_SHORT).show();
                } else {
                    bookChapterApi.addBannedWord(bannedWord).enqueue(new RetrofitCallBack<NoDataResponse>() {
                        @Override
                        public void onSuccess(NoDataResponse response) {
                            if (response.getCode() == 100){
                                bannedWords.add(bannedWord);
                                adapterBannedWord.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            }
        });

        dialog.show();
    }

    private void setBannedWordList() {
        unAuthApi.getBannedWordAdmin().enqueue(new RetrofitCallBack<GetBannedWordResponse>() {
            @Override
            public void onSuccess(GetBannedWordResponse response) {
                if (response.getCode() == 100){
                    bannedWords = response.getData().getWords();
                    adapterBannedWord = new AdapterBannedWord(bannedWords);
                    binding.bannedWordRv.setAdapter(adapterBannedWord);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }
}