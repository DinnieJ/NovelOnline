package com.example.novelonlineapp.model.hakore;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.novelonlineapp.model.hakore.chapter.Chapter;

import java.util.ArrayList;

public class Volume implements Parcelable {
    private String title;
    private ArrayList<Chapter> chapters;

    public Volume() {
        this.chapters = new ArrayList<>();
    }

    public Volume(String title, ArrayList<Chapter> chapters) {
        this.title = title;
        this.chapters = chapters;
    }

    protected Volume(Parcel in) {
        title = in.readString();
        chapters = in.createTypedArrayList(Chapter.CREATOR);
    }

    public static final Creator<Volume> CREATOR = new Creator<Volume>() {
        @Override
        public Volume createFromParcel(Parcel in) {
            return new Volume(in);
        }

        @Override
        public Volume[] newArray(int size) {
            return new Volume[size];
        }
    };

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
        dest.writeString(title);
        dest.writeList(this.chapters);
    }
}
