package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.BookChapter;

public class GetWarningListData {
    private BookChapter chapter;
    private BookChapter chapterReport;
    private Book book;
    private Book bookReport;

    public GetWarningListData(BookChapter chapter, BookChapter chapterReport, Book book, Book bookReport) {
        this.chapter = chapter;
        this.chapterReport = chapterReport;
        this.book = book;
        this.bookReport = bookReport;
    }

    public GetWarningListData() {
    }

    public BookChapter getChapter() {
        return chapter;
    }

    public void setChapter(BookChapter chapter) {
        this.chapter = chapter;
    }

    public BookChapter getChapterReport() {
        return chapterReport;
    }

    public void setChapterReport(BookChapter chapterReport) {
        this.chapterReport = chapterReport;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBookReport() {
        return bookReport;
    }

    public void setBookReport(Book bookReport) {
        this.bookReport = bookReport;
    }
}
