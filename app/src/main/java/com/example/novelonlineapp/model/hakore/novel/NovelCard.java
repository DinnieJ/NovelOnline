package com.example.novelonlineapp.model.hakore.novel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NovelCard implements Parcelable {
    @SerializedName("code")
    private String code;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String pageUrl;
    @SerializedName("latest_chapter")
    private String latestChapter;
    @SerializedName("latest_chapter_url")
    private String latestChapterUrl;
    @SerializedName("latest_vol")
    private String latestVolume;
    @SerializedName("img_url")
    private String coverImg;

    public NovelCard() {

    }

    public NovelCard(String code, String title, String pageUrl, String latestChapter, String latestChapterUrl, String latestVolume, String coverImg) {
        this.code = code;
        this.title = title;
        this.pageUrl = pageUrl;
        this.latestChapter = latestChapter;
        this.latestChapterUrl = latestChapterUrl;
        this.latestVolume = latestVolume;
        this.coverImg = coverImg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(String latestChapter) {
        this.latestChapter = latestChapter;
    }

    public String getLatestChapterUrl() {
        return latestChapterUrl;
    }

    public void setLatestChapterUrl(String latestChapterUrl) {
        this.latestChapterUrl = latestChapterUrl;
    }

    public String getLatestVolume() {
        return latestVolume;
    }

    public void setLatestVolume(String latestVolume) {
        this.latestVolume = latestVolume;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.title);
        dest.writeString(this.coverImg);
        dest.writeString(this.pageUrl);
        dest.writeString(this.latestChapter);
        dest.writeString(this.latestChapterUrl);
        dest.writeString(this.latestVolume);
    }

    protected NovelCard(Parcel in) {
        this.code = in.readString();
        this.title = in.readString();
        this.coverImg = in.readString();
        this.pageUrl = in.readString();
        this.latestChapter = in.readString();
        this.latestChapterUrl = in.readString();
        this.latestVolume = in.readString();
    }

    public static final Creator<NovelCard> CREATOR = new Creator<NovelCard>() {
        @Override
        public NovelCard createFromParcel(Parcel source) {
            return new NovelCard(source);
        }

        @Override
        public NovelCard[] newArray(int size) {
            return new NovelCard[size];
        }
    };
}
