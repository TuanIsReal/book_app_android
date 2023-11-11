package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.RowCategoryBinding;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.response.NoDataResponse;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> {

    private Context context;
    public ArrayList<Category> categories;

    private RowCategoryBinding binding;

    public AdapterCategory(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, @SuppressLint("RecyclerView") int position) {
        Category category = categories.get(position);
        String id = category.getId();
        String categoryName = category.getCategoryName();

        holder.categoryTv.setText(categoryName);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryApi.deleteCategory(id).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        categories.remove(position);
                        if (response.getCode() == 100){
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    class HolderCategory extends RecyclerView.ViewHolder {
        TextView categoryTv;
        ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;
        }
    }

}
