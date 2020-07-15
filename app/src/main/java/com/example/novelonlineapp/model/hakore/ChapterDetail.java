package com.example.novelonlineapp.model.hakore;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChapterDetail implements Parcelable {
    @SerializedName("content")
    List<String> content;
    @SerializedName("prev")
    String prevUrl;
    @SerializedName("next")
    String nextUrl;

    public ChapterDetail() {

    }

    public ChapterDetail(List<String> content, String prevUrl, String nextUrl) {
        this.content = content;
        this.prevUrl = prevUrl;
        this.nextUrl = nextUrl;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(String prevUrl) {
        this.prevUrl = prevUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    protected ChapterDetail(Parcel in) {
        content = in.createStringArrayList();
        prevUrl = in.readString();
        nextUrl = in.readString();
    }

    public static final Creator<ChapterDetail> CREATOR = new Creator<ChapterDetail>() {
        @Override
        public ChapterDetail createFromParcel(Parcel in) {
            return new ChapterDetail(in);
        }

        @Override
        public ChapterDetail[] newArray(int size) {
            return new ChapterDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(content);
        dest.writeString(prevUrl);
        dest.writeString(nextUrl);
    }
}
