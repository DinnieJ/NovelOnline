package com.example.novelonlineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.adapter.ChapterDetailViewAdapter;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.model.hakore.Chapter;
import com.example.novelonlineapp.model.hakore.ChapterDetail;
import com.example.novelonlineapp.utils.OnSwipeTouchListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadChapterActivity extends AppCompatActivity {

    ActionBar toolbar;
    HakoreApiService service;

    RecyclerView rv;
    BottomNavigationView bottomNavigationView;
    ProgressBar chapterLoading;
    TextView chapterTitle;
    RelativeLayout chapterDetailLayout;

    ChapterDetailViewAdapter chapterDetailViewAdapter;
    LinearLayoutManager linearLayoutManager;

    String prevUrl;
    String nextUrl;
    String currentUrl;
    Bundle dataFromNovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);

        toolbar = getSupportActionBar();
        rv = (RecyclerView) findViewById(R.id.chapter_content_view);
        chapterTitle = (TextView) findViewById(R.id.chapter_title);
        chapterLoading = (ProgressBar) findViewById(R.id.chapter_loading);
        chapterDetailViewAdapter = new ChapterDetailViewAdapter(this);
        chapterDetailLayout = (RelativeLayout) findViewById(R.id.chapter_content_layout);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        service = BaseApi.getClient().create(HakoreApiService.class);
        dataFromNovel = this.getIntent().getExtras();
        if (dataFromNovel != null) {
            currentUrl = dataFromNovel.getString("url");
            chapterTitle.setText(dataFromNovel.getString("title"));
        }

        rv.setLayoutManager(linearLayoutManager);

        loadData(currentUrl);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.chapter_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_next_chapter:
                        loadNextChapter();
                        break;
                    case R.id.btn_prev_chapter:
                        loadPreviousChapter();
                        break;
                }
                return false;
            }
        });

        rv.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                loadNextChapter();
            }
            public void onSwipeRight() {
                loadPreviousChapter();
            }
        });
        rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(oldScrollY > scrollY) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                else {
                    bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadNextChapter() {
        if(nextUrl != "") {
            Toast.makeText(getApplicationContext(), nextUrl, Toast.LENGTH_SHORT).show();
            chapterDetailViewAdapter.clear();
            loadData(nextUrl);
        } else {
            Toast.makeText(getApplicationContext(), "There is no more chapter ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPreviousChapter() {
        if(prevUrl != "") {
            Toast.makeText(getApplicationContext(), "Previous chapter", Toast.LENGTH_SHORT).show();
            chapterDetailViewAdapter.clear();
            loadData(prevUrl);
        } else {
            Toast.makeText(getApplicationContext(), "First chapter ", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadData(String url) {
        chapterLoading.setVisibility(View.VISIBLE);
        callChapterDetailApi(url).enqueue(new Callback<ChapterDetail>() {
            @Override
            public void onResponse(Call<ChapterDetail> call, Response<ChapterDetail> response) {
                ChapterDetail chapterDetail = response.body();
                chapterDetailViewAdapter.addAll(chapterDetail.getContent());
                rv.setAdapter(chapterDetailViewAdapter);
                prevUrl = chapterDetail.getPrevUrl();
                nextUrl = chapterDetail.getNextUrl();
                chapterLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ChapterDetail> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<ChapterDetail> callChapterDetailApi(String url) {
        return service.getChapterDetails(url);
    }

}