package com.anhtuan.bookapp.domain;

public class WarningChapter {
    private String chapter;

    private String chapterReport;

    private Double similarDocument;

    private Double similarWord;

    public WarningChapter() {
    }

    public WarningChapter(String chapter, String chapterReport, Double similarDocument, Double similarWord) {
        this.chapter = chapter;
        this.chapterReport = chapterReport;
        this.similarDocument = similarDocument;
        this.similarWord = similarWord;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getChapterReport() {
        return chapterReport;
    }

    public void setChapterReport(String chapterReport) {
        this.chapterReport = chapterReport;
    }

    public Double getSimilarDocument() {
        return similarDocument;
    }

    public void setSimilarDocument(Double similarDocument) {
        this.similarDocument = similarDocument;
    }

    public Double getSimilarWord() {
        return similarWord;
    }

    public void setSimilarWord(Double similarWord) {
        this.similarWord = similarWord;
    }
}
