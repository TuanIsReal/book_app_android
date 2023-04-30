package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.databinding.RowCategoryOptionBinding;
import com.anhtuan.bookapp.domain.Category;

import java.util.List;
import java.util.Map;

public class AdapterCategoryOption extends RecyclerView.Adapter<AdapterCategoryOption.HolderCategoryOption>{
    private Context context;
    public List<Category> categoryList;
    private RowCategoryOptionBinding binding;
    public Map<String, Boolean> statusCategory;
    public CategoryOptionListener categoryOptionListener;

    public AdapterCategoryOption(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setStatusCategory(Map<String, Boolean> statusCategory) {
        this.statusCategory = statusCategory;
        notifyDataSetChanged();
    }

    public void setCategoryOptionListener(CategoryOptionListener categoryOptionListener) {
        this.categoryOptionListener = categoryOptionListener;
    }

    @NonNull
    @Override
    public HolderCategoryOption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryOptionBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategoryOption(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategoryOption holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());
        boolean status = statusCategory.get(category.getId());
        if (status){
            holder.categoryName.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class HolderCategoryOption extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryName;

        public HolderCategoryOption(@NonNull View itemView) {
            super(itemView);
            categoryName = binding.categoryName;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            categoryOptionListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface CategoryOptionListener{
        void onItemClick(View view,int position);
    }
}
