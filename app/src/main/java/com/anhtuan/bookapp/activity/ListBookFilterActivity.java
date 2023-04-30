package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterBookFilter;
import com.anhtuan.bookapp.adapter.AdapterCategoryOption;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityListBookFilterBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.request.GetBookFilterRequest;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.GetBookResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBookFilterActivity extends AppCompatActivity implements AdapterCategoryOption.CategoryOptionListener {

    ActivityListBookFilterBinding binding;
    List<Book> listBookFilter;
    List<Category> categoryList;
    LinearLayoutManager manager;
    AdapterBookFilter adapterBookFilter;
    AdapterCategoryOption adapterCategoryOption;
    Map<String, Boolean> statusCategory;
    int sortOption;
    int statusOption;
    int postOption;
    List<String> categoryOptions;
    int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBookFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.optionSv.setVisibility(View.GONE);
        binding.filterBookBtn.setVisibility(View.GONE);
        manager = new LinearLayoutManager(this);
        categoryOptions = new ArrayList<>();
        order = -1;
        statusOption = 0;
        postOption = 0;

        Intent intent = getIntent();
        int filterType = intent.getIntExtra("filterType", 0);
        switch (filterType){
            case 1:
                sortOption = Constant.FILTER_SORT.SORT_BY_TIME;
                break;
            case 2:
                sortOption = Constant.FILTER_SORT.SORT_BY_STAR;
                break;
            case 3:
                sortOption = Constant.FILTER_SORT.SORT_BY_PURCHASED;
                break;
            case 4:
                sortOption = Constant.FILTER_SORT.SORT_BY_REVIEW;
                break;
            default:
                sortOption = Constant.FILTER_SORT.SORT_BY_TIME;
                break;
        }
        loadFirstOpen();

        binding.closeOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOption();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void openOption(){
        sortOption = 1;
        statusOption = 0;
        postOption = 0;
        binding.optionSv.setVisibility(View.VISIBLE);
        binding.filterBookBtn.setVisibility(View.VISIBLE);
        binding.timeDesc.setTypeface(null, Typeface.BOLD);
        binding.allStatus.setTypeface(null, Typeface.BOLD);
        binding.allPost.setTypeface(null, Typeface.BOLD);

        loadSortClick();
        loadStatusClick();
        loadPostClick();

        statusCategory = new HashMap<>();
        if (categoryList.size() > 0){
            for (Category category: categoryList){
                statusCategory.put(category.getId(), false);
            }
        }

        adapterCategoryOption = new AdapterCategoryOption(ListBookFilterActivity.this, categoryList);
        adapterCategoryOption.setStatusCategory(statusCategory);
        adapterCategoryOption.setCategoryOptionListener(ListBookFilterActivity.this);
        binding.categoryRv.setAdapter(adapterCategoryOption);

        binding.filterBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBook();
            }
        });
    }

    private void closeOption(){
        UIClickSort(sortOption, -1);
        UIClickStatus(statusOption, -1);
        UIClickPost(postOption, -1);
        binding.optionSv.setVisibility(View.GONE);
        binding.filterBookBtn.setVisibility(View.GONE);
    }

    private void loadFirstOpen(){
        GetBookFilterRequest request = new GetBookFilterRequest(sortOption, order, statusOption, postOption, new ArrayList<>(), 1);
        bookApi.getBookFilter(request).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    listBookFilter = response.body().getData();
                    setBookRv();
                    loadCategory();
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });
    }

    private void filterBook(){
        GetBookFilterRequest request = new GetBookFilterRequest();
        switch (sortOption){
            case 1:
                request.setSort(Constant.FILTER_SORT.SORT_BY_TIME);
                request.setOrder(-1);
                break;
            case 2:
                request.setSort(Constant.FILTER_SORT.SORT_BY_TIME);
                request.setOrder(1);
                break;
            case 3:
                request.setSort(Constant.FILTER_SORT.SORT_BY_PURCHASED);
                request.setOrder(-1);
                break;
            case 4:
                request.setSort(Constant.FILTER_SORT.SORT_BY_STAR);
                request.setOrder(-1);
                break;
            case 5:
                request.setSort(Constant.FILTER_SORT.SORT_BY_REVIEW);
                request.setOrder(-1);
                break;
            case 6:
                request.setSort(Constant.FILTER_SORT.SORT_BY_CHAPTER);
                request.setOrder(-1);
                break;
            case 7:
                request.setSort(Constant.FILTER_SORT.SORT_BY_PRICE);
                request.setOrder(-1);
                break;
            case 8:
                request.setSort(Constant.FILTER_SORT.SORT_BY_PRICE);
                request.setOrder(1);
            default:
                request.setSort(Constant.FILTER_SORT.SORT_BY_TIME);
                request.setOrder(-1);
                break;
        }

        switch (statusOption){
            case 0:
                request.setStatus(0);
                break;
            case 1:
                request.setStatus(1);
                break;
            case 2:
               request.setStatus(2);
                break;
            default:
                request.setStatus(0);
                break;
        }

        switch (postOption){
            case 0:
                request.setPost(0);
                break;
            case 1:
                request.setPost(1);
                break;
            case 2:
                request.setPost(2);
                break;
            default:
                request.setPost(0);
                break;
        }

        categoryOptions = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry: statusCategory.entrySet()){
            if (entry.getValue()){
                categoryOptions.add(entry.getKey());
            }
        }
        request.setCategory(categoryOptions);

        request.setPage(1);

        bookApi.getBookFilter(request).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    listBookFilter = response.body().getData();
                    adapterBookFilter.setBooks(listBookFilter);
                    closeOption();
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {

            }
        });

    }

    private void setBookRv(){
        binding.booksRv.setLayoutManager(manager);
        adapterBookFilter = new AdapterBookFilter(ListBookFilterActivity.this, listBookFilter);
        binding.booksRv.setAdapter(adapterBookFilter);
    }

    private void loadCategory(){
        categoryApi.getCategory().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    categoryList = response.body().getData();
                    binding.optionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openOption();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {

            }
        });
    }

    private void loadSortClick(){
        binding.timeDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 1){
                    UIClickSort(sortOption, 1);
                    sortOption = 1;
                }
            }
        });
        binding.timeAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 2){
                    UIClickSort(sortOption, 2);
                    sortOption = 2;
                }
            }
        });
        binding.purchasedDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 3){
                    UIClickSort(sortOption, 3);
                    sortOption = 3;
                }
            }
        });
        binding.starDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 4){
                    UIClickSort(sortOption, 4);
                    sortOption = 4;
                }
            }
        });
        binding.reviewDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 5){
                    UIClickSort(sortOption, 5);
                    sortOption = 5;
                }
            }
        });
        binding.chapterDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 6){
                    UIClickSort(sortOption, 6);
                    sortOption = 6;
                }
            }
        });
        binding.priceDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 7){
                    UIClickSort(sortOption, 7);
                    sortOption = 7;
                }
            }
        });
        binding.priceAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortOption != 8){
                    UIClickSort(sortOption, 8);
                    sortOption = 8;
                }
            }
        });
    }

    private void loadStatusClick(){
        binding.allStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusOption != 0){
                    UIClickStatus(statusOption, 0);
                    statusOption = 0;
                }
            }
        });
        binding.writingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusOption != 1){
                    UIClickStatus(statusOption, 1);
                    statusOption = 1;
                }
            }
        });
        binding.completeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusOption != 2){
                    UIClickStatus(statusOption, 2);
                    statusOption = 2;
                }
            }
        });
    }

    private void loadPostClick(){
        binding.allPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postOption != 0){
                    UIClickPost(postOption, 0);
                    postOption = 0;
                }
            }
        });
        binding.userPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postOption != 1){
                    UIClickPost(postOption, 1);
                    postOption = 1;
                }
            }
        });
        binding.adminPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postOption != 2){
                    UIClickPost(postOption, 2);
                    postOption = 2;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Category category = categoryList.get(position);
        Boolean status = statusCategory.get(category.getId());
        if (status){
            statusCategory.put(category.getId(), false);
        } else {
            statusCategory.put(category.getId(), true);
        }
        adapterCategoryOption.setStatusCategory(statusCategory);
    }

    private void UIClickSort(int oldSort, int newSort){
        switch (oldSort){
            case 1:
                binding.timeDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 2:
                binding.timeAsc.setTypeface(null, Typeface.NORMAL);
                break;
            case 3:
                binding.purchasedDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 4:
                binding.starDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 5:
                binding.reviewDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 6:
                binding.chapterDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 7:
                binding.priceDesc.setTypeface(null, Typeface.NORMAL);
                break;
            case 8:
                binding.priceAsc.setTypeface(null, Typeface.NORMAL);
            default:
                break;
        }

        switch (newSort){
            case 1:
                binding.timeDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                binding.timeAsc.setTypeface(null, Typeface.BOLD);
                break;
            case 3:
                binding.purchasedDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 4:
                binding.starDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 5:
                binding.reviewDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 6:
                binding.chapterDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 7:
                binding.priceDesc.setTypeface(null, Typeface.BOLD);
                break;
            case 8:
                binding.priceAsc.setTypeface(null, Typeface.BOLD);
            default:
                break;
        }
    }

    private void UIClickStatus(int oldStatus, int newStatus){
        switch (oldStatus){
            case 0:
                binding.allStatus.setTypeface(null, Typeface.NORMAL);
                break;
            case 1:
                binding.writingStatus.setTypeface(null, Typeface.NORMAL);
                break;
            case 2:
                binding.completeStatus.setTypeface(null, Typeface.NORMAL);
                break;
            default:
                break;
        }

        switch (newStatus){
            case 0:
                binding.allStatus.setTypeface(null, Typeface.BOLD);
                break;
            case 1:
                binding.writingStatus.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                binding.completeStatus.setTypeface(null, Typeface.BOLD);
                break;
            default:
                break;
        }
    }

    private void UIClickPost(int oldPost, int newPost){
        switch (oldPost){
            case 0:
                binding.allPost.setTypeface(null, Typeface.NORMAL);
                break;
            case 1:
                binding.userPost.setTypeface(null, Typeface.NORMAL);
                break;
            case 2:
                binding.adminPost.setTypeface(null, Typeface.NORMAL);
                break;
            default:
                break;
        }

        switch (newPost){
            case 0:
                binding.allPost.setTypeface(null, Typeface.BOLD);
                break;
            case 1:
                binding.userPost.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                binding.adminPost.setTypeface(null, Typeface.BOLD);
                break;
            default:
                break;
        }
    }
}