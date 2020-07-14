package com.example.novelonlineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.novelonlineapp.adapter.VolumeDetailAdapter;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.model.hakore.Volume;
import com.example.novelonlineapp.model.hakore.novel.Novel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovelDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView cover;
    TextView title;
    TextView author;
    TextView artist;
    ProgressBar titleProgress;
    ProgressBar mainProgressBar;
    HakoreApiService service;
    VolumeDetailAdapter volumeDetailAdapter;
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;

    String urlDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_novel_detail);

        cover = (ImageView) findViewById(R.id.cover_image);
        cover.setColorFilter(new PorterDuffColorFilter(Color.argb(80,0,0,0), PorterDuff.Mode.SRC_OVER));
        title = (TextView) findViewById(R.id.novel_title);
        titleProgress = (ProgressBar) findViewById(R.id.cover_progress);
        mainProgressBar = (ProgressBar) findViewById(R.id.main_progress_detail) ;
        service = BaseApi.getClient().create(HakoreApiService.class);
        rv = (RecyclerView) findViewById(R.id.volume_detail_layout);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        volumeDetailAdapter = new VolumeDetailAdapter(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(volumeDetailAdapter);
        author = (TextView) findViewById(R.id.author_name);
        artist = (TextView) findViewById(R.id.artist_name);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("title"));
            this.urlDetail = bundle.getString("url");

            Glide.with(this).load(bundle.getString("cover"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            titleProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            titleProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .crossFade()
                    .into(cover);
            title.setText(bundle.getString("title"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 500);
        }


    }

    private Call<Novel> callGetNovelDetailApi() {
        return service.getNovelDetails(this.urlDetail);
    }

    private void loadData() {
        callGetNovelDetailApi().enqueue(new Callback<Novel>() {
            @Override
            public void onResponse(Call<Novel> call, Response<Novel> response) {
                Novel result = response.body();
                artist.setText("Họa sĩ:" +  result.getArtist());
                author.setText("Tác giả:" + result.getAuthor());
                volumeDetailAdapter.clear();
                volumeDetailAdapter.addAll(result.getVols());
                mainProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Novel> call, Throwable t) {

            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}