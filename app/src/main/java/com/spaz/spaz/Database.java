package com.spaz.spaz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asyaky on 26/11/2016.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Spaz.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DB_NAMA_TABEL = "tugas";
    public static final String DB_COLUMN_ID = "_id";
    public static final String DB_COLUMN_TIPE = "tipe";
    public static final String DB_COLUMN_NAMA = "nama";
    public static final String DB_COLUMN_KONTEN = "konten";
    public static final String DB_COLUMN_TIME = "time";
    public static final String DB_COLUMN_FREQUENCY = "frequency";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_NAMA_TABEL + "(" +
                DB_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                DB_COLUMN_TIPE + " TEXT, " +
                DB_COLUMN_NAMA + " TEXT, " +
                DB_COLUMN_KONTEN + " TEXT, " +
                DB_COLUMN_FREQUENCY + " TEXT, " +
                DB_COLUMN_TIME + " LONG)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAMA_TABEL);
        onCreate(db);
    }

    public boolean insertNote(String title, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TIPE, "note");
        values.put(DB_COLUMN_NAMA, title);
        values.put(DB_COLUMN_KONTEN, content);
        db.insert(DB_NAMA_TABEL, null, values);
        return true;
    }

    public long insertAlert(String title, String content, long time, int frequency) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TIPE, "alert");
        values.put(DB_COLUMN_NAMA, title);
        values.put(DB_COLUMN_KONTEN, content);
        values.put(DB_COLUMN_TIME, time);
        values.put(DB_COLUMN_FREQUENCY, frequency);
        return db.insert(DB_NAMA_TABEL, null, values);
    }

    public boolean updateNote(Integer id, String title, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_KONTEN, note);
        values.put(DB_COLUMN_NAMA, title);
        db.update(DB_NAMA_TABEL, values, DB_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateAlert(Integer id, String title, String note, long time, int frequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_KONTEN, note);
        values.put(DB_COLUMN_NAMA, title);
        values.put(DB_COLUMN_TIME, time);
        values.put(DB_COLUMN_FREQUENCY, frequency);
        db.update(DB_NAMA_TABEL, values, DB_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateTime(Integer id, long time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TIME, time);
        db.update(DB_NAMA_TABEL, values, DB_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_NAMA_TABEL + " WHERE " +
                DB_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_NAMA_TABEL + " ORDER BY " + DB_COLUMN_ID + " DESC", null);
    }
    public Cursor getAllAlerts(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_NAMA_TABEL + " WHERE " +
                DB_COLUMN_TIPE + " = ? ", new String[]{"alert"});
    }
    public Cursor getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_NAMA_TABEL + " WHERE " +
                DB_COLUMN_TIPE + " = ? ", new String[]{"note"});
    }
    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_NAMA_TABEL, DB_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public boolean isEmpty() {
        return getAllItems().getCount() == 0;
    }

}
