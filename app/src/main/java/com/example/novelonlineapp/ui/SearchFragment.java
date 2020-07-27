package com.example.novelonlineapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.novelonlineapp.R;
import com.example.novelonlineapp.api.BaseApi;
import com.example.novelonlineapp.api.HakoreApiService;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    HakoreApiService service;
    FlexboxLayout flexbox;
    View view;
    ImageButton searchBtn;
    EditText searchField;
    FlexboxLayoutManager flexboxLayoutManager;
    private Map<String, String> selectedGenre;
    private Map<String, String> ignoredGenre;
    private final static int GENRE_DEFAULT = 0;
    private final static int GENRE_SELECTED = 1;
    private final static int GENRE_IGNORED = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        service = BaseApi.getClient().create(HakoreApiService.class);
        flexbox = (FlexboxLayout) view.findViewById(R.id.flexbox_genre);
        searchBtn = (ImageButton) view.findViewById(R.id.search_btn);
        selectedGenre = new HashMap<String, String>();
        ignoredGenre = new HashMap<String, String>();
        searchField = (EditText) view.findViewById(R.id.search_field);


        flexboxLayoutManager = new FlexboxLayoutManager(getFragmentContext());
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), buildSearchUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        loadData();
        return view;
    }

    private Context getFragmentContext() {
        return view.getContext();
    }

    private void loadData() {
        service.getGenreFilter().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonData = response.body();
                Set<Map.Entry<String, JsonElement>> entries = jsonData.entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    Button b = new Button(getFragmentContext());
                    b.setText(entry.getKey());
                    b.setTag(R.string.genre_tag, entry.getValue().getAsString());
                    b.setTag(R.string.genre_status, GENRE_DEFAULT);
                    b.setTextColor(getResources().getColor(R.color.colorPrimary));
                    b.setBackground(getResources().getDrawable(R.drawable.button_bg_default));
                    b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setPadding(15, 5, 15, 5);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch ((Integer) v.getTag(R.string.genre_status)) {
                                case GENRE_DEFAULT:
                                    selectGenre(v);
                                    break;
                                case GENRE_SELECTED:
                                    ignoreGenre(v);
                                    break;
                                case GENRE_IGNORED:
                                    unselectGenre(v);
                                    break;
                            }
                        }
                    });
                    flexbox.addView(b);
                }
                searchBtn.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void unselectGenre(View v) {
        Button b = (Button) v;
        b.setTextColor(getResources().getColor(R.color.colorTextGenreDefault));
        b.setBackground(getResources().getDrawable(R.drawable.button_bg_default));
        b.setTag(R.string.genre_status, GENRE_DEFAULT);

        String key = b.getText().toString();
        ignoredGenre.remove(key);
    }

    private void selectGenre(View v) {
        Button b = (Button) v;
        b.setTextColor(getResources().getColor(R.color.colorTextGenreSelected));
        b.setBackground(getResources().getDrawable(R.drawable.button_bg_selected));
        b.setTag(R.string.genre_status, GENRE_SELECTED);

        String key = b.getText().toString();
        String value = (String) b.getTag(R.string.genre_tag);
        selectedGenre.put(key, value);
    }

    private void ignoreGenre(View v) {
        Button b = (Button) v;
        b.setTextColor(getResources().getColor(R.color.colorTextGenreSelected));
        b.setBackground(getResources().getDrawable(R.drawable.button_bg_ignored));
        b.setTag(R.string.genre_status, GENRE_IGNORED);

        String key = b.getText().toString();
        String value = (String) b.getTag(R.string.genre_tag);
        selectedGenre.remove(key);
        ignoredGenre.put(key, value);
    }

    private String buildSearchUrl() {
        String selectedString = String.join(",", selectedGenre.values());
        String ignoredString = String.join(",", ignoredGenre.values());
        String keyword = searchField.getText().toString().replaceAll("\\s+", "");

        String searchUrl = "/api/hakore/search?keyword=" + keyword + "&selected=" + selectedString + "&ignore=" + ignoredString;

        return searchUrl;
    }
}