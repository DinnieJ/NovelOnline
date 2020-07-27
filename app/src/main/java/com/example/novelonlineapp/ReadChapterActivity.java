package com.example.novelonlineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.adapter.ChapterDetailViewAdapter;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.dao.DatabaseHandler;
import com.example.novelonlineapp.model.hakore.chapter.ChapterDetail;
import com.example.novelonlineapp.model.hakore.history.History;
import com.example.novelonlineapp.utils.OnSwipeTouchListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadChapterActivity extends AppCompatActivity {

    ActionBar toolbar;
    HakoreApiService service;

    RecyclerView rv;
    ProgressBar chapterLoading;
    TextView chapterTitle;
    RelativeLayout chapterDetailLayout;

    ChapterDetailViewAdapter chapterDetailViewAdapter;
    LinearLayoutManager linearLayoutManager;

    String prevUrl;
    String nextUrl;
    String currentUrl;
    Bundle dataFromNovel;
    private int mTotalScrolled = 0;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        checkFirstRun();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        toolbar = getSupportActionBar();
        rv = (RecyclerView) findViewById(R.id.chapter_content_view);
        chapterTitle = (TextView) findViewById(R.id.chapter_title);
        chapterLoading = (ProgressBar) findViewById(R.id.chapter_loading);
        chapterDetailViewAdapter = new ChapterDetailViewAdapter(this);
        chapterDetailLayout = (RelativeLayout) findViewById(R.id.chapter_content_layout);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        service = BaseApi.getClient().create(HakoreApiService.class);
        dataFromNovel = this.getIntent().getExtras();
        db = new DatabaseHandler(this);
        if (dataFromNovel != null) {
            currentUrl = dataFromNovel.getString("url");
            chapterTitle.setText(dataFromNovel.getString("title"));
        }
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(linearLayoutManager);


        loadData(currentUrl);

        rv.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                loadNextChapter();
            }

            @Override
            public void onSwipeTop() {

            }

            @Override
            public void onSwipeBottom() {

            }

            public void onSwipeRight() {
                loadPreviousChapter();
            }
        });
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;
            }
        });
    }

    private void loadNextChapter() {
        if (nextUrl != "") {
            Toast.makeText(getApplicationContext(), "Next chapter", Toast.LENGTH_SHORT).show();
            chapterDetailViewAdapter.clear();
            loadData(nextUrl);
        } else {
            Toast.makeText(getApplicationContext(), "There is no more chapter ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPreviousChapter() {
        if (prevUrl != "") {
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

    @Override
    public void finish() {
        super.finish();
        createHistory();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        createHistory();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void createHistory() {
        db.addHistory(new History(dataFromNovel.getString("novel-code"), dataFromNovel.getString("novel"), dataFromNovel.getString("title"), currentUrl, mTotalScrolled, dataFromNovel.getString("cover")));
        this.mTotalScrolled = 0;
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            new AlertDialog.Builder(this).setTitle("First time here ?").setMessage("Swipe left and right to change the chapter").setNeutralButton("OK", null).show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
}