package com.example.novelonlineapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.R;
import com.example.novelonlineapp.model.hakore.chapter.Chapter;
import com.example.novelonlineapp.model.hakore.Volume;

import java.util.ArrayList;
import java.util.List;

public class VolumeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Volume> data;
    private Context context;

    public VolumeDetailAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder = null;
        View v1 = inflater.inflate(R.layout.volume_card, parent, false);
        viewHolder = new VolumeCardVH(v1);
        return viewHolder;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(parent, LayoutInflater.from(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Volume result = this.data.get(position);
        VolumeCardVH volumeCardVH = (VolumeCardVH) holder;

        volumeCardVH.volumeTitle.setText(result.getTitle());
        System.err.println(result.getTitle());
        for (Chapter c : result.getChapters()
        ) {
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableRow.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(context);
            textView.setText(c.getTitle());
            tr.addView(textView);
            final String chapterUrl = c.getUrl();
            System.out.println(c.getTitle());
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, chapterUrl, Toast.LENGTH_SHORT).show();
                }
            });
            volumeCardVH.chapterTable.addView(tr);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void add(Volume v) {
        data.add(v);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<Volume> vols) {
        for (Volume vol : vols) {
            add(vol);
        }
    }

    public void remove(Volume v) {
        int position = data.indexOf(v);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Volume getItem(int position) {
        return data.get(position);
    }

    protected class VolumeCardVH extends RecyclerView.ViewHolder {
        private TableLayout chapterTable;
        private TextView volumeTitle;

        public VolumeCardVH(@NonNull View itemView) {
            super(itemView);
            chapterTable = (TableLayout) itemView.findViewById(R.id.chapter_table);
            volumeTitle = (TextView) itemView.findViewById(R.id.volume_title);
        }
    }


}
