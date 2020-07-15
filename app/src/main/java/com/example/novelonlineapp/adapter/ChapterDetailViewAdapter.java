package com.example.novelonlineapp.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChapterDetailViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private final static int LINE = 0;
    private final static int IMAGE = 1;

    private List<String> chapterContent;

    private final static Pattern pattern = Pattern.compile("^--img--\\[(.*)\\]");

    public ChapterDetailViewAdapter(Context context) {
        this.context = context;
        chapterContent = new ArrayList<String>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        System.out.println("on Create View hOlder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LINE:
                viewHolder = getLineViewHolder(parent, inflater);
                System.out.println("line");
                break;
            case IMAGE:
                viewHolder = getImageViewHolder(parent, inflater);
                System.out.println("image");
                break;
        }

        return viewHolder;
    }

    private RecyclerView.ViewHolder getImageViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = inflater.inflate(R.layout.chapter_content_image, parent, false);
        viewHolder = new ImageVH(v);
        return viewHolder;
    }

    private RecyclerView.ViewHolder getLineViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = inflater.inflate(R.layout.chapter_content_line, parent, false);
        viewHolder = new LineVH(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String line = this.chapterContent.get(position);
        System.out.println("Debug 1: " + line);
        switch (getItemViewType(position)) {
            case LINE:
                final LineVH lineVH = (LineVH) holder;
                lineVH.line.setText(line);
                System.out.println("add-line");
                break;
            case IMAGE:
                final ImageVH imageVH = (ImageVH) holder;
                Matcher m = pattern.matcher(line);
                String imgUrl = "";
                if (m.find()) {
                    imgUrl = m.group(1);
                }
                Glide.with(this.context)
                        .load(imgUrl)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                imageVH.loadingImage.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                imageVH.loadingImage.setVisibility(View.GONE);
                                return false;
                            }
                        }).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(imageVH.image);
                System.out.println("add-image");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chapterContent.size();
    }


    @Override
    public int getItemViewType(int position) {
        Matcher matcher = pattern.matcher(chapterContent.get(position));
        return matcher.matches() ? IMAGE : LINE;
    }

    public void add(String line) {
        this.chapterContent.add(line);
    }

    public void addAll(List<String> ls) {
        for (String s : ls) {
            add(s);
        }
    }

    public List<String> getContent() {
        return chapterContent;
    }

    public void clear() {
        chapterContent.clear();
    }

    protected class LineVH extends RecyclerView.ViewHolder {
        private TextView line;

        public LineVH(@NonNull View itemView) {
            super(itemView);
            this.line = (TextView) itemView.findViewById(R.id.chapter_line);
        }
    }

    protected class ImageVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private ProgressBar loadingImage;

        public ImageVH(@NonNull View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.chapter_image);
            this.loadingImage = (ProgressBar) itemView.findViewById(R.id.chapter_image_progress);
        }
    }
}
