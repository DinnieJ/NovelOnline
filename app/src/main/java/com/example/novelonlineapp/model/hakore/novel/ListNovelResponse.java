package com.example.novelonlineapp.model.hakore.novel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListNovelResponse implements Parcelable {
    @SerializedName("books")
    private ArrayList<NovelCard> results;
    @SerializedName("count")
    private int count;
    @SerializedName("prevPage")
    private String previousPageUrl;
    @SerializedName("nextPage")
    private String nextPageUrl;
    @SerializedName("current")
    private int currentPage;
    @SerializedName("lastPage")
    private int lastPage;
    @SerializedName("lastPageUrl")
    private String lastPageUrl;

    public ListNovelResponse() {
    }

    public ArrayList<NovelCard> getResults() {
        return results;
    }

    public void setResults(ArrayList<NovelCard> results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPreviousPageUrl() {
        return previousPageUrl;
    }

    public void setPreviousPageUrl(String previousPageUrl) {
        this.previousPageUrl = previousPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
    }

    public ListNovelResponse(ArrayList<NovelCard> results, int count, String previousPageUrl, String nextPageUrl, int currentPage, int lastPage, String lastPageUrl) {
        this.results = results;
        this.count = count;
        this.previousPageUrl = previousPageUrl;
        this.nextPageUrl = nextPageUrl;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.lastPageUrl = lastPageUrl;
    }

    protected ListNovelResponse(Parcel in) {
        this.count = in.readInt();
        this.results = in.createTypedArrayList(NovelCard.CREATOR);
        this.previousPageUrl = in.readString();
        this.nextPageUrl = in.readString();
        this.currentPage = in.readInt();
        this.lastPage = in.readInt();
        this.lastPageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<ListNovelResponse> CREATOR = new Creator<ListNovelResponse>() {
        @Override
        public ListNovelResponse createFromParcel(Parcel source) {
            return new ListNovelResponse(source);
        }

        @Override
        public ListNovelResponse[] newArray(int size) {
            return new ListNovelResponse    [size];
        }
    };
}
