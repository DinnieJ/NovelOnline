package com.example.novelonlineapp.model.hakore;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Volume implements Parcelable {
    private String title;
    private ArrayList<Chapter> chapters;

    public Volume() {

    }

    public Volume(String title, ArrayList<Chapter> chapters) {
        this.title = title;
        this.chapters = chapters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
