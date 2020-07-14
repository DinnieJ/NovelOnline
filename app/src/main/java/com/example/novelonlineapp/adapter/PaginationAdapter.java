package com.example.novelonlineapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.novelonlineapp.NovelDetailActivity;
import com.example.novelonlineapp.R;
import com.example.novelonlineapp.model.hakore.novel.NovelCard;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int TAG_TITLE = View.generateViewId() + 2 << 24;
    private Toast toast;

    private List<NovelCard> novelResults;

    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        this.novelResults = new ArrayList<NovelCard>();
    }

    public List<NovelCard> getNovelResults() {
        return novelResults;
    }

    public void setNovelResults(List<NovelCard> novelResults) {
        this.novelResults = novelResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 =   inflater.inflate(R.layout.progress_circle, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == this.novelResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fragment_card, parent, false);
        viewHolder = new NovelCardVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NovelCard result = this.novelResults.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final NovelCardVH novelCardVH = (NovelCardVH) holder;
                String title = "";
                if(result.getTitle().length() > 20) {
                    title = result.getTitle().substring(0,20) + "...";
                } else {
                    title = result.getTitle();
                }
                String chapter = "";
                if(result.getLatestChapter().length() > 28) {
                    chapter = result.getLatestChapter().substring(0,28) + "...";
                } else {
                    chapter = result.getLatestChapter();
                }
                novelCardVH.title.setText(title);
                novelCardVH.chapter.setText(chapter);
                Glide.with(this.context)
                        .load(result.getCoverImg())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                novelCardVH.progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                novelCardVH.progress.setVisibility(View.GONE);
                                return false;
                            }
                        }).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(novelCardVH.coverImg);
                novelCardVH.coverImg.setColorFilter(new PorterDuffColorFilter(Color.argb(80,0,0,0), PorterDuff.Mode.SRC_OVER));
                novelCardVH.card.setTag(TAG_TITLE, result.getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", result.getPageUrl());
                        bundle.putString("title", result.getTitle());
                        bundle.putString("cover", result.getCoverImg());
                        Intent openDetail = new Intent(context, NovelDetailActivity.class);
                        openDetail.putExtras(bundle);
                        context.startActivity(openDetail);
                    }
                });
                break;
            case LOADING:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return this.novelResults == null ? 0 : this.novelResults.size();
    }

    //Helper

    public void add(NovelCard nc) {
        novelResults.add(nc);
        notifyItemInserted(novelResults.size() - 1);
    }

    public void addAll(List<NovelCard> ncs) {
        for (NovelCard nc : ncs) {
            add(nc);
        }
    }

    public void remove(NovelCard nc) {
        int position = novelResults.indexOf(nc);
        if(position > -1) {
            novelResults.remove(nc);
            notifyItemRemoved(position);
        }
    }

    public NovelCard getNovelCard(int pos) {
        return novelResults.get(pos);
    }

    public void clear() {
        isLoadingAdded = false;
        while(getItemCount() > 0) {
            remove(getNovelCard(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NovelCard());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = novelResults.size() - 1;
        NovelCard result = getNovelCard(position);

        if (result != null) {
            novelResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    protected class NovelCardVH extends RecyclerView.ViewHolder {
        private ImageView coverImg;
        private TextView title;
        private TextView chapter;
        private ProgressBar progress;
        private CardView card;

        public NovelCardVH(@NonNull View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card);
            coverImg = (ImageView) itemView.findViewById(R.id.card_cover);
            title = (TextView) itemView.findViewById(R.id.card_title);
            chapter = (TextView) itemView.findViewById(R.id.card_chapter);
            progress = (ProgressBar) itemView.findViewById(R.id.card_progress);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
