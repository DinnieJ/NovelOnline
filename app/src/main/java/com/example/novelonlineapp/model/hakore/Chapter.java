package com.example.novelonlineapp.model.hakore;

import java.util.ArrayList;

public class Chapter {
    private ArrayList<String> content;
    private String nextChapterUrl;
    private String prevChapterUrl;

    public Chapter() {

    }

    public Chapter(ArrayList<String> content, String nextChapterUrl, String prevChapterUrl) {
        this.content = content;
        this.nextChapterUrl = nextChapterUrl;
        this.prevChapterUrl = prevChapterUrl;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public String getNextChapterUrl() {
        return nextChapterUrl;
    }

    public void setNextChapterUrl(String nextChapterUrl) {
        this.nextChapterUrl = nextChapterUrl;
    }

    public String getPrevChapterUrl() {
        return prevChapterUrl;
    }

    public void setPrevChapterUrl(String prevChapterUrl) {
        this.prevChapterUrl = prevChapterUrl;
    }
}
