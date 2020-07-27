package com.example.novelonlineapp.api;

import com.example.novelonlineapp.model.hakore.chapter.ChapterDetail;
import com.example.novelonlineapp.model.hakore.novel.ListNovelResponse;
import com.example.novelonlineapp.model.hakore.novel.Novel;
import com.google.gson.JsonObject;

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

    @GET("/api/hakore/genrefilter")
    Call<JsonObject> getGenreFilter();

    @GET("api/hakore/search")
    Call<ListNovelResponse> searchNovel(@Query("selected") String selected, @Query("ignore") String ignore, @Query("page") int page, @Query("keyword") String keyword);

}
