package com.example.novelonlineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.novelonlineapp.adapter.VolumeDetailAdapter;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.model.hakore.chapter.Chapter;
import com.example.novelonlineapp.model.hakore.Volume;
import com.example.novelonlineapp.model.hakore.novel.Novel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovelDetailActivity extends AppCompatActivity {
    ImageView cover;
    TextView title;
    TextView description;
    TextView author;
    TextView artist;
    ProgressBar titleProgress;
    ProgressBar mainProgressBar;
    HakoreApiService service;
    VolumeDetailAdapter volumeDetailAdapter;
    TableLayout tableLayout;
    ScrollView scrollView;
    FloatingActionButton backToTopBtn;

    String urlDetail;
    Bundle dataFromCard;
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
        volumeDetailAdapter = new VolumeDetailAdapter(this);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        author = (TextView) findViewById(R.id.author_name);
        artist = (TextView) findViewById(R.id.artist_name);
        description = (TextView) findViewById(R.id.description_text);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        backToTopBtn = (FloatingActionButton) findViewById(R.id.btn_back_to_top);
        backToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if(scrollY > 50 && backToTopBtn.getVisibility() == View.GONE) {
                    backToTopBtn.setVisibility(View.VISIBLE);
                    backToTopBtn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_in));
                } else if( scrollY < 50 && backToTopBtn.getVisibility() == View.VISIBLE)  {
                    backToTopBtn.setVisibility(View.GONE);
                    backToTopBtn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_out));
                }
            }
        });
        dataFromCard = this.getIntent().getExtras();
        if(dataFromCard != null) {
            getSupportActionBar().setTitle(dataFromCard.getString("title"));
            this.urlDetail = dataFromCard.getString("url");

            Glide.with(this).load(dataFromCard.getString("cover"))
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
            title.setText(dataFromCard.getString("title"));
            loadData();
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
                for(String line: result.getDescription()) {
                    description.append(line + "\n");
                }
                for(final Volume volume: result.getVols()) {
                    TableRow row = new TableRow(getApplicationContext());
                    final LayoutInflater row_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View volumeDetail = row_inflater.inflate(R.layout.volume_card, null);
                    TextView title = volumeDetail.findViewById(R.id.volume_title);
                    TableLayout chapterView = volumeDetail.findViewById(R.id.chapter_table);
                    title.setText(volume.getTitle());
                    for(final Chapter chapter: volume.getChapters()) {
                        TableRow chapterRow = new TableRow(volumeDetail.getContext());
                        chapterRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        chapterRow.setPadding(5,20,5,20);
                        chapterRow.setBackgroundColor(getColor(R.color.colorChapterRow));
                        TextView text = new TextView(volumeDetail.getContext());
                        text.setMaxEms(20);
                        text.setSingleLine(false);
                        text.setText(chapter.getTitle());
                        chapterRow.addView(text);
                        chapterRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("novel-code", result.getCode());
                                bundle.putString("title", chapter.getTitle());
                                bundle.putString("url", chapter.getUrl());
                                bundle.putString("volume-title", volume.getTitle());
                                bundle.putString("chapter-code", chapter.getCode());
                                bundle.putString("novel", dataFromCard.getString("title"));
                                bundle.putString("cover", result.getImgUrl());

                                Intent chapterDetailIntent = new Intent(getApplicationContext(), ReadChapterActivity.class);
                                chapterDetailIntent.putExtras(bundle);
                                startActivity(chapterDetailIntent);
                            }
                        });
                        chapterView.addView(chapterRow);
                    }
                    row.addView(volumeDetail);
                    tableLayout.addView(row);
                }
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