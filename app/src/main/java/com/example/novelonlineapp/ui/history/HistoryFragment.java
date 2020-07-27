package com.example.novelonlineapp.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novelonlineapp.R;
import com.example.novelonlineapp.adapter.HistoryViewAdapter;
import com.example.novelonlineapp.dao.DatabaseHandler;
import com.example.novelonlineapp.model.hakore.history.History;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {

    DatabaseHandler db;
    RecyclerView rv;
    HistoryViewAdapter adapter;
    LinearLayoutManager layoutManager;
    FloatingActionButton clearHistoryBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        adapter = new HistoryViewAdapter(view.getContext());
        db = new DatabaseHandler(view.getContext());
        rv = (RecyclerView) view.findViewById(R.id.history_view);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        clearHistoryBtn = (FloatingActionButton) view.findViewById(R.id.clear_history_btn);

        clearHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearHistory();
                Toast.makeText(view.getContext(), "History cleared", Toast.LENGTH_SHORT).show();
            }
        });

        List<History> histories = db.getAllHistory();
        for (History h : histories
        ) {
            System.out.println(h);
        }
        Collections.reverse(histories);
        adapter.addAll(histories);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return view;
    }
}
