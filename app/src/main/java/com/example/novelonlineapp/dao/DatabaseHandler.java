package com.example.novelonlineapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.novelonlineapp.model.hakore.history.History;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "novelonlineapp";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "histories";

    private static final String HISTORY_NOVEL_CODE = "novel_code";
    private static final String HISTORY_NOVEL_TITLE = "novel_title";
    private static final String HISTORY_NOVEL_CHAPTER = "novel_chapter";
    private static final String HISTORY_NOVEL_CHAPTER_URL = "novel_chapterUrl";
    private static final String HISTORY_SCROLL_LOCATION= "scrollLocation";
    private static final String HISTORY_NOVEL_COVER = "coverUrl";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(this.getWritableDatabase());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createHistoryTable= String.format("CREATE TABLE IF NOT EXISTS %s(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s INT, %s TEXT )", TABLE_NAME,
                                        HISTORY_NOVEL_CODE,
                                        HISTORY_NOVEL_TITLE,
                                        HISTORY_NOVEL_CHAPTER,
                                        HISTORY_NOVEL_CHAPTER_URL,
                                        HISTORY_SCROLL_LOCATION,
                                        HISTORY_NOVEL_COVER);
        System.out.println("created");
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropHistoryTable = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(dropHistoryTable);
        onCreate(db);
    }

    public void addHistory(History history) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor cursor = dbRead.query(TABLE_NAME, null, HISTORY_NOVEL_CODE + " = ?", new String[] { String.valueOf(history.getNovelCode()) },null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0) {
            add(history);
        } else {
            update(history);
        }
    }

    public void add(History history) {
        ContentValues values = new ContentValues();
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        values.put(HISTORY_NOVEL_CODE, history.getNovelCode());
        values.put(HISTORY_NOVEL_TITLE, history.getNovel_title());
        values.put(HISTORY_NOVEL_CHAPTER, history.getNovel_chapter());
        values.put(HISTORY_NOVEL_CHAPTER_URL, history.getNovel_chapterUrl());
        values.put(HISTORY_SCROLL_LOCATION, history.getScrollLocation());
        values.put(HISTORY_NOVEL_COVER, history.getCoverUrl());
        dbWrite.insert(TABLE_NAME, null, values);
        dbWrite.close();
    }


    public void update(History history) {
        ContentValues values = new ContentValues();
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        values.put(HISTORY_NOVEL_TITLE, history.getNovel_title());
        values.put(HISTORY_NOVEL_CHAPTER, history.getNovel_chapter());
        values.put(HISTORY_NOVEL_CHAPTER_URL, history.getNovel_chapterUrl());
        values.put(HISTORY_SCROLL_LOCATION, history.getScrollLocation());
        values.put(HISTORY_NOVEL_COVER, history.getCoverUrl());
        dbWrite.update(TABLE_NAME, values, HISTORY_NOVEL_CODE + "=?", new String[] {String.valueOf(history.getNovelCode())});
        dbWrite.close();
    }

    public List<History> getAllHistory() {
        java.util.List<History> histories = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            History history = new History(cursor.getString(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getInt(4),
                                        cursor.getString(5));
            histories.add(history);
            cursor.moveToNext();
        }
        return histories;
    }

    public void clearHistory() {
        String query = String.format("DELETE FROM %s WHERE 1=1", TABLE_NAME);
        this.getWritableDatabase().execSQL(query);
    }

    public void dropTable() {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
