package com.example.novelonlineapp.model.hakore.novel;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.novelonlineapp.model.hakore.Volume;

import java.util.ArrayList;

public class Novel implements Parcelable {
    private String code;
    private String title;
    private String imgUrl;
    private ArrayList<String> description;
    private ArrayList<String> genres;
    private String author;
    private String artist;
    private ArrayList<Volume> vols;

    public Novel() {
        this.description = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Novel(String code, String title, String imgUrl, ArrayList<String> description, ArrayList<String> genres, String author, String artist) {
        this.code = code;
        this.title = title;
        this.imgUrl = imgUrl;
        this.description = description;
        this.genres = genres;
        this.author = author;
        this.artist = artist;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected Novel(Parcel in) {

    }
    public static final Creator<Novel> CREATOR = new Creator<Novel>() {
        @Override
        public Novel createFromParcel(Parcel source) {
            return new Novel(source);
        }

        @Override
        public Novel[] newArray(int size) {
            return new Novel[size];
        }
    };
}
