package com.anhtuan.bookapp.domain;

import java.util.List;

public class BannedWord {
    private int version;

    private List<String> words;

    public BannedWord() {
    }

    public BannedWord(int version, List<String> words) {
        this.version = version;
        this.words = words;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
