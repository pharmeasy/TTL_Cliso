package com.example.e5322.thyrosoft.SqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.e5322.thyrosoft.Models.TRFModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Offline_Woe.db";
    public static final String TABLE_NAME = "woe";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "BARCODES";
    public static final String COL_3 = "WOEJSON";
    public static final String COL_4 = "ERROR";

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,BARCODES TEXT,WOEJSON TEXT,ERROR TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, barcodes);
        contentValues.put(COL_3, woejson);


        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor selectData(String barcodes) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COL_3 + " FROM " + TABLE_NAME + " WHERE BARCODES = " + "'" + barcodes.trim() + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateData(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, woejson);

        db.update(TABLE_NAME, contentValues, "BARCODES = ?", new String[]{barcodes});
        return true;
    }

    public boolean updateDataeRROR(String barcodes, String error) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, error);

        db.update(TABLE_NAME, contentValues, "BARCODES = ?", new String[]{barcodes});
        return true;
    }


    public boolean updateBArcodeColumn(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, barcodes);

        db.update(TABLE_NAME, contentValues, "WOEJSON = ?", new String[]{woejson});
        return true;
    }

    public boolean deleteData(String barcodes) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_2 + "=?", new String[]{barcodes}) > 0;
    }

    public void open() throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
