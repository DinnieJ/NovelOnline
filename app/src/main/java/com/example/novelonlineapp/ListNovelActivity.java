package com.example.novelonlineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class ListNovelActivity extends AppCompatActivity {
    GridLayoutManager gridLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("hello world 2");
        View view = findViewById(R.layout.fragment_all);
    }
}