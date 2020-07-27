package com.example.novelonlineapp.model.hakore.history;


public class History{
    private String novelCode;
    private String novel_title;
    private String novel_chapter;
    private String novel_chapterUrl;
    private int scrollLocation;
    private String coverUrl;


    public History(String novelCode, String novel_title, String novel_chapter, String novel_chapterUrl, int scrollLocation, String coverUrl) {
        this.novelCode = novelCode;
        this.novel_title = novel_title;
        this.novel_chapter = novel_chapter;
        this.novel_chapterUrl = novel_chapterUrl;
        this.scrollLocation = scrollLocation;
        this.coverUrl = coverUrl;
    }

    public String getNovelCode() {
        return novelCode;
    }

    public void setNovelCode(String novelCode) {
        this.novelCode = novelCode;
    }

    public String getNovel_title() {
        return novel_title;
    }

    public void setNovel_title(String novel_title) {
        this.novel_title = novel_title;
    }

    public String getNovel_chapter() {
        return novel_chapter;
    }

    public void setNovel_chapter(String novel_chapter) {
        this.novel_chapter = novel_chapter;
    }

    public String getNovel_chapterUrl() {
        return novel_chapterUrl;
    }

    public void setNovel_chapterUrl(String novel_chapterUrl) {
        this.novel_chapterUrl = novel_chapterUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getScrollLocation() {
        return scrollLocation;
    }

    public void setScrollLocation(int scrollLocation) {
        this.scrollLocation = scrollLocation;
    }

    @Override
    public String toString() {
        return "History{" +
                "novelCode='" + novelCode + '\'' +
                ", novel_title='" + novel_title + '\'' +
                ", novel_chapter='" + novel_chapter + '\'' +
                ", novel_chapterUrl='" + novel_chapterUrl + '\'' +
                ", scrollLocation=" + scrollLocation +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
