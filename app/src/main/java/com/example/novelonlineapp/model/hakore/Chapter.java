package com.example.novelonlineapp.model.hakore;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Chapter implements Parcelable {
    @SerializedName("code")
    private String code;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public Chapter() {

    }

    public Chapter(String code, String title, String url) {
        this.code = code;
        this.title = title;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected Chapter(Parcel in) {
        code = in.readString();
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(title);
        dest.writeString(url);
    }
}
