package com.example.novelonlineapp.ui.all;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.adapter.PaginationAdapter;
import com.example.novelonlineapp.R;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.example.novelonlineapp.model.hakore.novel.ListNovelResponse;
import com.example.novelonlineapp.model.hakore.novel.NovelCard;
import com.example.novelonlineapp.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllFragment extends Fragment {


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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        adapter = new PaginationAdapter(view.getContext());
        rv = view.findViewById(R.id.recycle_view_all);

        progressBar = view.findViewById(R.id.main_progress);
        gridLayoutManager = new GridLayoutManager(view.getContext(), ITEM_PER_ROW);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        service = BaseApi.getClient().create(HakoreApiService.class);

        loadFirstPage();
        rv.setOnScrollListener(new PaginationScrollListener(gridLayoutManager, view.getContext()) {
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
        return view;
    }

    private void loadFirstPage() {
        callAllNovelApi().enqueue(new Callback<ListNovelResponse>() {
            @Override
            public void onResponse(Call<ListNovelResponse> call, Response<ListNovelResponse> response) {

                List<NovelCard> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                maxPage = getMaxPage(response);
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
}
