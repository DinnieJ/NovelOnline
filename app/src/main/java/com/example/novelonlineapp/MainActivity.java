package com.example.novelonlineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.model.hakore.novel.ListNovelResponse;
import com.example.novelonlineapp.model.hakore.novel.NovelCard;
import com.example.novelonlineapp.ui.all.AllFragment;
import com.example.novelonlineapp.ui.history.HistoryFragment;
import com.example.novelonlineapp.utils.PaginationScrollListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    GridLayoutManager gridLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private HakoreApiService service;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private int maxPage = 0;
    private final int ITEM_PER_ROW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PaginationAdapter(this);
        rv = findViewById(R.id.recycle_view_all);

        progressBar = findViewById(R.id.main_progress);
        gridLayoutManager = new GridLayoutManager(this, ITEM_PER_ROW);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        service = BaseApi.getClient().create(HakoreApiService.class);

        loadFirstPage();
        rv.setOnScrollListener(new PaginationScrollListener(gridLayoutManager, this) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return maxPage;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                this.drawer,
                toolbar,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }
    private void loadFirstPage() {
        callAllNovelApi().enqueue(new Callback<ListNovelResponse>() {
            @Override
            public void onResponse(Call<ListNovelResponse> call, Response<ListNovelResponse> response) {

                List<NovelCard> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                maxPage = 3;
                if (currentPage <= maxPage) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ListNovelResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private void loadNextPage() {
        callAllNovelApi().enqueue(new Callback<ListNovelResponse>() {
            @Override
            public void onResponse(Call<ListNovelResponse> call, Response<ListNovelResponse> response) {
                // Got data. Send it to adapter
                adapter.removeLoadingFooter();
                isLoading = false;
                List<NovelCard> results =  fetchResults(response);
                adapter.addAll(results);
                if(currentPage != maxPage) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ListNovelResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private Call<ListNovelResponse> callAllNovelApi() {
        return service.getAll(this.currentPage);
    }

    private List<NovelCard> fetchResults(Response<ListNovelResponse> response) {
        ListNovelResponse listNovel = response.body();
        return listNovel.getResults();
    }

    private int getMaxPage(Response<ListNovelResponse> response) {
        ListNovelResponse listNovel = response.body();
        return listNovel.getLastPage();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllFragment()).commit();
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
                break;
        }
        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}