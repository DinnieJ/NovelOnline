package com.example.novelonlineapp.api;

import com.example.novelonlineapp.model.hakore.ChapterDetail;
import com.example.novelonlineapp.model.hakore.novel.ListNovelResponse;
import com.example.novelonlineapp.model.hakore.novel.Novel;
import com.example.novelonlineapp.model.hakore.novel.NovelCard;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface HakoreApiService {
    @GET("/api/hakore/all")
    Call<ListNovelResponse> getAll(@Query("page") int page);

    @GET
    Call<Novel> getNovelDetails(@Url String url);

    @GET
    Call<ChapterDetail> getChapterDetails(@Url String url);
}
