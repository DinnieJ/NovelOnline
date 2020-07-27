package com.example.novelonlineapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.novelonlineapp.R;
import com.example.novelonlineapp.ReadChapterActivity;
import com.example.novelonlineapp.model.hakore.history.History;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<History> historyList;

    public HistoryViewAdapter(Context context) {
        this.context = context;
        this.historyList = new ArrayList<>();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View historyCard = inflater.inflate(R.layout.history_card, parent, false);
        viewHolder = new HistoryCardVH(historyCard);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final History result = historyList.get(position);
        final HistoryCardVH viewHolder = (HistoryCardVH) holder;

        viewHolder.title.setText(result.getNovel_title());
        viewHolder.chapter.setText(result.getNovel_chapter());
        Glide.with(context)
                .load(result.getCoverUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        viewHolder.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        viewHolder.progress.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(viewHolder.cover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("novel-code", result.getNovelCode());
                bundle.putString("title", result.getNovel_chapter());
                bundle.putString("url", result.getNovel_chapterUrl());
                bundle.putString("novel", result.getNovel_title());
                bundle.putString("cover", result.getCoverUrl());

                Intent intent = new Intent(context, ReadChapterActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
    public void add(History h) {
        this.historyList.add(h);
    }

    public void addAll(List<History> ls) {
        for (History s : ls) {
            add(s);
        }
    }


    public void clear() {
        historyList.clear();
    }

    protected class HistoryCardVH extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView chapter;
        ProgressBar progress;
        public HistoryCardVH(@NonNull View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.history_cover);
            title = (TextView) itemView.findViewById(R.id.history_title);
            chapter = (TextView) itemView.findViewById(R.id.history_chapter);
            progress = (ProgressBar) itemView.findViewById(R.id.history_cover_progress);
        }
    }
}
