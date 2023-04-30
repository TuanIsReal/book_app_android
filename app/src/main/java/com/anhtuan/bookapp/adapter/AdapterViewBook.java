package com.anhtuan.bookapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.anhtuan.bookapp.fragment.ViewBookChapterListFragment;
import com.anhtuan.bookapp.fragment.ViewBookInfoFragment;
import com.anhtuan.bookapp.fragment.ViewBookReviewFragment;

public class AdapterViewBook extends FragmentStatePagerAdapter {
    int numPage;
    String userId;
    String bookId;
    String introduction;
    String author;
    boolean isPurchased;

    public AdapterViewBook(@NonNull FragmentManager fm, int behavior, String userId, String bookId, String introduction, String author, boolean isPurchased) {
        super(fm, behavior);
        this.userId = userId;
        this.bookId = bookId;
        this.introduction = introduction;
        this.author = author;
        this.isPurchased = isPurchased;
        numPage = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ViewBookInfoFragment(bookId, introduction, author);
            case 1:
                return new ViewBookReviewFragment(bookId, isPurchased);
            case 2:
                return new ViewBookChapterListFragment(bookId, userId);
            default:
                return new ViewBookInfoFragment(bookId, introduction, author);
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Giới thiệu";
            case 1:
                return "Đánh giá";
            case 2:
                return "D.S Chương";
            default:
                return "Giới thiệu";
        }
    }
}
